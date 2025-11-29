package com.shomari.flashcardbe.service;

import org.springframework.stereotype.Service;

import com.shomari.flashcardbe.entity.FlashcardSet;
import com.shomari.flashcardbe.repository.FlashcardSetRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlashcardSetService {
    private final FlashcardSetRepository repository;

    public FlashcardSetService(FlashcardSetRepository repository) {
        this.repository = repository;
    }

    public List<FlashcardSet> getSetsByUser(String userId) {
        return repository.findByUserId(userId);
    }

    public FlashcardSet createSet(String userId, String name) {
        FlashcardSet set = new FlashcardSet();
        set.setName(name);
        set.setUserId(userId);
        return repository.save(set);
    }

    public FlashcardSet getSetByIdAndUser(Long setId, String userId) {
        return repository.findById(setId)
                .filter(set -> set.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Set not found"));
    }

    public void deleteSet(Long setId, String userId) {
        FlashcardSet set = getSetByIdAndUser(setId, userId);
        repository.delete(set);
    }
}
