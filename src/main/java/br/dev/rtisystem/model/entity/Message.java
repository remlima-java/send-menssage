package br.dev.rtisystem.model.entity;


import br.dev.rtisystem.model.MessageDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "sender", nullable = false)
    private String from;

    @Column(name = "receiver", nullable = false)
    private String to;
    private String content;

    public Message(MessageDto message) {
        this.id = message.getId();
        this.from = message.getFrom();
        this.to = message.getTo();
        this.content = message.getContent();
    }

    public Message() {
    }
}
