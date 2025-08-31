package br.dev.rtisystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "senders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;
}
