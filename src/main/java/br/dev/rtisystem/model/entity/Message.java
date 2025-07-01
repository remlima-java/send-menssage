package br.dev.rtisystem.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "sender", nullable = false)
    private String from;

    @Column(name = "receiver", nullable = false)
    private String to;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User sender;
    private final LocalDateTime timestamp = LocalDateTime.now();

}
