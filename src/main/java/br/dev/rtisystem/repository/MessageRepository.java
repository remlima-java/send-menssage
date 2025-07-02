package br.dev.rtisystem.repository;

import br.dev.rtisystem.model.dtos.MessageDtozao;
import br.dev.rtisystem.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(value = "select * from senders s \n" +
            "inner join senders_messages sm on s.id = sm.senders_id\n" +
            "inner join message m on sm.messages_id = m.id", nativeQuery = true)
    List<MessageDtozao> findMessageJoin();
}
