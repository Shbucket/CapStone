package com.shomari.flashcardbe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="flashcards",
        indexes = {@Index(columnList = "userId")})
public class Flashcard extends BaseEntity {

    @NotBlank(message = "Topic is required")
    private String topic;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Question is required")
    private String question;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Answer is required")
    private String answer;

    @Column(nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "flashcard_set_id", nullable = false)
    @JsonBackReference
    private FlashcardSet flashcardSet;

    public Flashcard() {}

    @Override
    public String getSummary() {
        return "Question: " + question + " | A: " + answer;
    }

    // getters and setters
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public FlashcardSet getFlashcardSet() { return flashcardSet; }
    public void setFlashcardSet(FlashcardSet flashcardSet) { this.flashcardSet = flashcardSet; }
}
