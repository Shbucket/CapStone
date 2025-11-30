//package com.shomari.flashcardbe.testing;
//import com.shomari.flashcardbe.entity.Flashcard;
//import com.shomari.flashcardbe.entity.FlashcardSet;
//import com.shomari.flashcardbe.repository.FlashcardRepository;
//import com.shomari.flashcardbe.repository.FlashcardSetRepository;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@Transactional
//public class FlashcardIntegrationTest {
//    @Autowired
//    private FlashcardRepository flashcardRepository;
//
//    @Autowired
//    private FlashcardSetRepository flashcardSetRepository;
//
//    // Helper method to create a FlashcardSet
//    private FlashcardSet createFlashcardSet(String name, String userId) {
//        FlashcardSet set = new FlashcardSet();
//        set.setName(name);
//        set.setUserId(userId);
//        return flashcardSetRepository.save(set);
//    }
//
//    // Helper method to create a Flashcard
//    private Flashcard createFlashcard(String topic, String question, String answer, String userId, FlashcardSet set) {
//        Flashcard flashcard = new Flashcard();
//        flashcard.setTopic(topic);
//        flashcard.setQuestion(question);
//        flashcard.setAnswer(answer);
//        flashcard.setUserId(userId);
//        flashcard.setFlashcardSet(set);
//        return flashcardRepository.save(flashcard);
//    }
//    //Find flashcards
//    @Test
//    void testCreateAndFindFlashcardSet() {
//        FlashcardSet set = createFlashcardSet("Math", "user1");
//
//        Optional<FlashcardSet> foundSet = flashcardSetRepository.findById(set.getId());
//        assertThat(foundSet).isPresent();
//        assertThat(foundSet.get().getName()).isEqualTo("Math");
//        assertThat(foundSet.get().getUserId()).isEqualTo("user1");
//    }
//
//    @Test
////    void testCreateAndFindFlashcard() {
////        FlashcardSet set = createFlashcardSet("Science", "user2");
////        Flashcard flashcard = createFlashcard("Physics", "What is gravity?", "Force pulling objects", "user2", set);
////
//////        Optional<Flashcard> foundFlashcard = flashcardRepository.findById(flashcard.getFlashcardId());
////        assertThat(foundFlashcard).isPresent();
////        assertThat(foundFlashcard.get().getTopic()).isEqualTo("Physics");
////        assertThat(foundFlashcard.get().getFlashcardSet().getId()).isEqualTo(set.getId());
////    }
//    //Find all in a set
//    @Test
//    void testFindAllFlashcardsInSet() {
//        FlashcardSet set = createFlashcardSet("History", "user3");
//        createFlashcard("Ancient Rome", "When was Rome founded?", "753 BC", "user3", set);
//        createFlashcard("Medieval Europe", "What started the Hundred Years War?", "Dispute over succession", "user3", set);
//
//        List<Flashcard> flashcards = flashcardRepository.findAllByFlashcardSetId(set.getId());
//        assertThat(flashcards).hasSize(2);
//    }
//    //update the flashcards
//    @Test
//    void testUpdateFlashcard() {
//        FlashcardSet set = createFlashcardSet("Geography", "user4");
//        Flashcard flashcard = createFlashcard("Earth", "How many continents?", "7", "user4", set);
//
//        flashcard.setAnswer("Seven");
//        Flashcard updated = flashcardRepository.save(flashcard);
//
//        assertThat(updated.getAnswer()).isEqualTo("Seven");
//    }
//    //delete the flashcards
//    @Test
//    void testDeleteFlashcard() {
//        FlashcardSet set = createFlashcardSet("Chemistry", "user5");
//        Flashcard flashcard = createFlashcard("H2O", "What is water?", "H2O", "user5", set);
//
//        flashcardRepository.delete(flashcard);
//        Optional<Flashcard> deleted = flashcardRepository.findById(flashcard.getFlashcardId());
//        assertThat(deleted).isEmpty();
//    }
//    //validation tests
//    @Test
//    void testFlashcardValidationConstraints() {
//        FlashcardSet set = createFlashcardSet("Languages", "user4");
//
//        Flashcard invalidFlashcard = new Flashcard();
//        invalidFlashcard.setTopic("");
//        invalidFlashcard.setQuestion("");
//        invalidFlashcard.setAnswer("");
//        invalidFlashcard.setUserId("user4");
//        invalidFlashcard.setFlashcardSet(set);
//
//        assertThrows(ConstraintViolationException.class, () -> {
//            flashcardRepository.saveAndFlush(invalidFlashcard);
//        });
//    }
//
//    @Test
//    void testFlashcardSetValidationConstraints() {
//        FlashcardSet invalidSet = new FlashcardSet();
//        invalidSet.setName("");
//        invalidSet.setUserId("user5");
//
//        assertThrows(ConstraintViolationException.class, () -> {
//            flashcardSetRepository.saveAndFlush(invalidSet);
//        });
//    }
//    //cascade delete
//    @Test
//    void testCascadeDeleteFlashcardSet() {
//        // create and save parent
//        FlashcardSet set = createFlashcardSet("Biology", "user3");
//        flashcardSetRepository.saveAndFlush(set);
//
//        // create children and add to parent
//        Flashcard flashcard1 = createFlashcard("Cell", "What is the basic unit of life?", "Cell", "user3", set);
//        Flashcard flashcard2 = createFlashcard("DNA", "What does DNA stand for?", "Deoxyribonucleic Acid", "user3", set);
//
//        set.getFlashcards().add(flashcard1);
//        set.getFlashcards().add(flashcard2);
//
//        flashcard1.setFlashcardSet(set);
//        flashcard2.setFlashcardSet(set);
//
//        flashcardSetRepository.saveAndFlush(set); // persist parent + children
//
//        // reload the parent to ensure it's managed
//        FlashcardSet managedSet = flashcardSetRepository.findById(set.getId()).get();
//
//        flashcardSetRepository.delete(managedSet);
//        flashcardSetRepository.flush();
//
//        assertThat(flashcardSetRepository.findById(set.getId())).isEmpty();
//        assertThat(flashcardRepository.findById(flashcard1.getFlashcardId())).isEmpty();
//        assertThat(flashcardRepository.findById(flashcard2.getFlashcardId())).isEmpty();
//    }
//    @Test
//    void testTimestampsAreSet() {
//        Flashcard flashcard = new Flashcard();
//        flashcard.setTopic("Physics");
//        flashcard.setQuestion("What is velocity?");
//        flashcard.setAnswer("Speed in a given direction");
//        flashcard.setUserId("user123");
//
//        flashcardRepository.saveAndFlush(flashcard);
//
//        assertThat(flashcard.getCreatedAt()).isNotNull();
//        assertThat(flashcard.getUpdatedAt()).isNotNull();
//    }
//}
