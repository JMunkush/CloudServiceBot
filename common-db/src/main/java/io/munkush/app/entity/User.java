package io.munkush.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tgId;
    @Column(unique = true)
    private String username;
    private String firstName;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private State state = State.AFK;
    @Builder.Default
    private LocalDateTime enterAt = LocalDateTime.now();
    @OneToMany
    @Builder.Default
    private List<Content> contents = new ArrayList<>();

}
