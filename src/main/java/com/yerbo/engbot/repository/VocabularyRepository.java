package com.yerbo.engbot.repository;

import com.yerbo.engbot.models.User;
import com.yerbo.engbot.models.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    boolean existsByUserAndEngWord(User user, String engWord);
}
