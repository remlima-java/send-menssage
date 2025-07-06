package br.dev.rtisystem.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity(name = "senders")
@Data
@NoArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;
}
