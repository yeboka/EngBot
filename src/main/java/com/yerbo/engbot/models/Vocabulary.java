package com.yerbo.engbot.models;

import com.yerbo.engbot.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "vocabulary", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "eng_word")
    private String engWord;

    @Column(name = "rus_word")
    private String rusWord;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}