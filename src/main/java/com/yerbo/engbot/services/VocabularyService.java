package com.yerbo.engbot.services;

import com.yerbo.engbot.models.User;
import com.yerbo.engbot.models.Vocabulary;
import com.yerbo.engbot.repository.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    @Autowired
    public VocabularyService(VocabularyRepository vocabularyRepository) {
        this.vocabularyRepository = vocabularyRepository;
    }

    public Page<Vocabulary> getPageOfWordsOfUser(User user, Integer page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        return vocabularyRepository.findAllByUser(user, pageable);
    }

    public void saveWord(User user, Vocabulary vocabulary) {
        if (vocabularyRepository.existsByUserAndEngWord(user, vocabulary.getEngWord())) {
            vocabularyRepository.save(vocabulary);
        }

    }
}
