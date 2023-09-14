package com.yerbo.engbot.models;

import com.yerbo.engbot.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "vocabulary", schema = "public")
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "word")
    private String word;

    @Column(name = "status")
    private Status status;
}