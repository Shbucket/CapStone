package com.shomari.flashcardbe.service;


import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.repository.FlashcardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class FlashcardServiceTest {
    @MockBean
    private FlashcardRepository flashcardRepository;

    @Autowired
    private FlashcardService flashcardService;

    @Test
    void testCreateFlashcard() {
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion("What is Java?");
        flashcard.setAnswer("A programming language");
        flashcard.setTopic("Programming");

        Mockito.when(flashcardRepository.save(Mockito.any(Flashcard.class))).thenReturn(flashcard);

        Flashcard saved = flashcardService.createFlashcard(flashcard);
        Assertions.assertEquals("What is Java?", saved.getQuestion());
    }
}
