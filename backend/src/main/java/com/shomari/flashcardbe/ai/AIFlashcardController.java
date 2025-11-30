package com.shomari.flashcardbe.ai;

import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.FlashcardSet;
import com.shomari.flashcardbe.repository.FlashcardRepository;
import com.shomari.flashcardbe.repository.FlashcardSetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIFlashcardController {

    private final AIFlashcardService aiService;
    private final FlashcardSetRepository setRepository;
    private final FlashcardRepository flashcardRepository;

    public AIFlashcardController(AIFlashcardService aiService,
                                 FlashcardSetRepository setRepository,
                                 FlashcardRepository flashcardRepository) {
        this.aiService = aiService;
        this.setRepository = setRepository;
        this.flashcardRepository = flashcardRepository;
    }

    /** Generate flashcards only */
    @PostMapping("/flashcards")
    public List<FlashcardDTO> generateFlashcards(@RequestBody FlashcardRequest request) {
        return aiService.generateFlashcards(request.getText(), request.getNumFlashcards());
    }

    /** Save flashcards as a named set */
    @PostMapping("/flashcards/set")
    public ResponseEntity<FlashcardSet> saveFlashcardsSet(
            @RequestParam String userId,
            @RequestBody SaveFlashcardSetRequest request) {

        // Create set
        FlashcardSet set = new FlashcardSet();
        set.setName(request.getSetName());
        set.setUserId(userId);
        setRepository.save(set);

        // Save each flashcard
        for (FlashcardDTO dto : request.getFlashcards()) {
            Flashcard fc = new Flashcard();
            fc.setTopic(request.getTopic());
            fc.setQuestion(dto.front());
            fc.setAnswer(dto.back());
            fc.setUserId(userId);
            fc.setFlashcardSet(set);

            flashcardRepository.save(fc);
            set.getFlashcards().add(fc);
        }

        return ResponseEntity.ok(set);
    }

    /** DTOs */
    public static class FlashcardRequest {
        private String text;
        private int numFlashcards;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public int getNumFlashcards() { return numFlashcards; }
        public void setNumFlashcards(int numFlashcards) { this.numFlashcards = numFlashcards; }
    }

    public static class SaveFlashcardSetRequest {
        private String setName;
        private String topic;
        private List<FlashcardDTO> flashcards;

        public String getSetName() { return setName; }
        public void setSetName(String setName) { this.setName = setName; }

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }

        public List<FlashcardDTO> getFlashcards() { return flashcards; }
        public void setFlashcards(List<FlashcardDTO> flashcards) { this.flashcards = flashcards; }
    }
}
