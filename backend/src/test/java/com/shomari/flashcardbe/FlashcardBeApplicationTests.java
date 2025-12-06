package com.shomari.flashcardbe;

import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.FlashcardSet;
import com.shomari.flashcardbe.repository.FlashcardRepository;
import com.shomari.flashcardbe.service.FlashcardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


class FlashcardBeApplicationTests {

	@Mock
	private FlashcardRepository flashcardRepository;

	@InjectMocks
	private FlashcardService flashcardService;

	private Flashcard flashcard;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		FlashcardSet set = new FlashcardSet();
		set.setName("Test Set");
		set.setUserId("user123");

		flashcard = new Flashcard();
		flashcard.setTopic("Math");
		flashcard.setQuestion("2+2?");
		flashcard.setAnswer("4");
		flashcard.setUserId("user123");
		flashcard.setFlashcardSet(set);
	}

	@Test
	void testCreateFlashcard() {
		when(flashcardRepository.save(flashcard)).thenReturn(flashcard);

		Flashcard created = flashcardService.createFlashcard(flashcard);

		assertNotNull(created);
		assertEquals("Math", created.getTopic());
		verify(flashcardRepository, times(1)).save(flashcard);
	}
	@Test
	void testDeleteFlashcard() {
		doNothing().when(flashcardRepository).delete(flashcard);

		flashcardService.deleteFlashcard(flashcard);

		verify(flashcardRepository, times(1)).delete(flashcard);
	}

	@Test
	void testUpdateFlashcard() {
		// Change the flashcard before updating
		flashcard.setAnswer("Four");

		when(flashcardRepository.save(flashcard)).thenReturn(flashcard);

		Flashcard updated = flashcardService.updateFlashcard(flashcard);

		assertNotNull(updated);
		assertEquals("Four", updated.getAnswer());
		verify(flashcardRepository, times(1)).save(flashcard);
	}
}
