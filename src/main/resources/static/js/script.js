document.addEventListener('DOMContentLoaded', function() {
    // Verificar autenticação
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');

    if (!token || !username) {
        window.location.href = '/login.html';
        return;
    }

    // Mostrar informações do usuário
    const userAvatar = document.getElementById('userAvatar');
    const userDisplay = document.getElementById('userDisplay');

    userAvatar.textContent = username.charAt(0).toUpperCase();
    userDisplay.textContent = username;

    // Campo "De" preenchido automaticamente com o username
    document.getElementById('from').value = username;

    // Logout
    document.getElementById('logoutBtn').addEventListener('click', function() {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        window.location.href = '/login.html';
    });

    // Resto do código
    const apiUrl = "/user";
    const messagesUrl = "/messages";
    const messageList = document.getElementById("messageList");
    const loadingIndicator = document.getElementById("loadingIndicator");

    // ID do elemento da mensagem "Seja o primeiro a enviar"
    let emptyMessageId = null;

    // Função para criar um elemento de mensagem
    function createMessageElement(message, isHistory = false) {
        const bubble = document.createElement("div");
        bubble.className = `chat-bubble list-group-item mb-2 ${isHistory ? 'history-message' : 'realtime-message'}`;

        bubble.innerHTML = `
                🗨️ De: ${message.emitter || 'Desconhecido'} → Para: ${message.recipient || 'Desconhecido'}<br>
                "${message.content || ''}"<br>
                <small class="text-muted">${message.timestamp ? new Date(message.timestamp).toLocaleString() : 'Sem data'}</small>
            `;
        return bubble;
    }

    // Carregar histórico de mensagens ao inicializar
    async function loadMessageHistory() {
        try {
            const response = await fetch(messagesUrl, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                if (response.status === 401 || response.status === 403) {
                    // Token inválido, redirecionar para login
                    localStorage.removeItem('token');
                    localStorage.removeItem('username');
                    window.location.href = '/login.html';
                    return;
                }

                throw new Error(`Erro ao carregar mensagens: ${response.status}`);
            }

            const messages = await response.json();

            // Remover indicador de carregamento
            if (loadingIndicator) {
                loadingIndicator.remove();
            }

            // Se não houver mensagens, mostrar mensagem
            if (!messages || messages.length === 0) {
                const emptyMessage = document.createElement("div");
                emptyMessage.id = "empty-message-notice"; // ID para identificar facilmente
                emptyMessageId = "empty-message-notice"; // Armazenar o ID para remoção posterior
                emptyMessage.className = "text-center text-muted my-4";
                emptyMessage.textContent = "Nenhuma mensagem encontrada. Seja o primeiro a enviar!";
                messageList.appendChild(emptyMessage);
                return;
            }

            console.log(`Carregadas ${messages.length} mensagens`);

            // Adicionar mensagens do histórico sem ordenar, pois o backend já retorna na ordem correta
            messages.forEach(message => {
                messageList.appendChild(createMessageElement(message, true));
            });

        } catch (error) {
            console.error("Erro ao carregar mensagens:", error);

            // Remover indicador de carregamento e mostrar erro
            if (loadingIndicator) {
                loadingIndicator.remove();
            }

            const errorMessage = document.createElement("div");
            errorMessage.className = "alert alert-danger";
            errorMessage.textContent = `Erro ao carregar mensagens: ${error.message}`;
            messageList.appendChild(errorMessage);
        }
    }

    // Carregar histórico quando a página for carregada
    loadMessageHistory();

    // Envio de novas mensagens
    document.getElementById("messageForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const msg = {
            messages: [{
                emitter: document.getElementById("from").value,
                recipient: document.getElementById("to").value,
                content: document.getElementById("content").value
            }]
        };

        try {
            const res = await fetch(apiUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(msg)
            });

            if (res.ok) {
                console.log("Mensagem enviada com sucesso");

                // Manter o campo "De", limpar os outros
                document.getElementById("to").value = "";
                document.getElementById("content").value = "";

                // Remover a mensagem "Seja o primeiro a enviar" quando uma nova mensagem é enviada
                if (emptyMessageId) {
                    const emptyElement = document.getElementById(emptyMessageId);
                    if (emptyElement) {
                        emptyElement.remove();
                    }
                    emptyMessageId = null; // Limpar o ID após a remoção
                }
            } else if (res.status === 401 || res.status === 403) {
                // Token inválido, redirecionar para login
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                window.location.href = '/login.html';
            } else {
                console.error("Erro ao enviar mensagem:", await res.text());
                alert("Erro ao enviar mensagem");
            }
        } catch (err) {
            console.error("Erro ao enviar:", err);
            alert(`Erro ao enviar: ${err.message}`);
        }
    });

    // SSE: escuta atualizações da fila Kafka
    let eventSource;

    function setupEventSource() {
        // Fechar conexão existente se houver
        if (eventSource) {
            eventSource.close();
        }

        // Criar nova conexão
        eventSource = new EventSource(token
            ? `/stream?token=${token}`
            : "/stream");

        eventSource.onopen = () => {
            console.log("Conexão SSE estabelecida");
        };

        eventSource.onerror = (error) => {
            console.error("Erro na conexão SSE:", error);
            // Reconectar após um tempo se houver erro
            setTimeout(() => {
                console.log("Tentando reconectar SSE...");
                setupEventSource();
            }, 5000);
        };

        eventSource.addEventListener("message", (event) => {
            console.log("Evento SSE recebido:", event.data);

            try {
                const user = JSON.parse(event.data);

                if (!user || !user.messages || !Array.isArray(user.messages)) {
                    console.warn("Dados inválidos recebidos:", user);
                    return;
                }

                // Remover a mensagem "Seja o primeiro a enviar" quando uma nova mensagem é recebida
                if (emptyMessageId) {
                    const emptyElement = document.getElementById(emptyMessageId);
                    if (emptyElement) {
                        emptyElement.remove();
                    }
                    emptyMessageId = null; // Limpar o ID após a remoção
                }

                user.messages.forEach(msg => {
                    if (!msg) {
                        console.warn("Mensagem inválida encontrada");
                        return;
                    }

                    // Importante: Adicionar no INÍCIO da lista (não no final)
                    const messageElement = createMessageElement(msg, false);
                    // Inserir no início da lista
                    messageList.insertBefore(messageElement, messageList.firstChild);

                    // Toast visual
                    document.getElementById("toastBody").textContent =
                        `Nova mensagem: ${msg.content || ''}`;

                    const toastEl = document.getElementById("liveToast");
                    const toast = new bootstrap.Toast(toastEl);
                    toast.show();
                });
            } catch (error) {
                console.error("Erro ao processar mensagem SSE:", error);
            }
        });
    }

    // Iniciar conexão SSE
    setupEventSource();

    // Limpar conexão ao sair da página
    window.addEventListener('beforeunload', function() {
        if (eventSource) {
            eventSource.close();
        }
    });
});