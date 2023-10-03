package com.yerbo.engbot.repository;

import com.yerbo.engbot.models.User;
import com.yerbo.engbot.models.Vocabulary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    boolean existsByUserAndEngWord(User user, String engWord);
    List<Vocabulary> getWordsByUser(User user);
    Page<Vocabulary> findAllByUser(User user, Pageable pageable);
}
