//package br.dev.rtisystem.service;
//
//import br.dev.rtisystem.model.dtos.MessageDtozao;
//import lombok.AllArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Service
//@AllArgsConstructor
//public class MessageConsumer {
//
//    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
//
//    @KafkaListener(topics = "chat-group", groupId = "chat-group")
//    public void listen(MessageDtozao message) {
//        for (SseEmitter emitter : emitters) {
//            try {
//                emitter.send(SseEmitter.event().name("message").data(message));
//                emitters.add(emitter);
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//                emitters.remove(emitter);
//            }
//        }
//    }
//
//}
