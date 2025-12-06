package com.shomari.flashcardbe.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIFlashcardService {

    private final ChatClient chatClient;


    public AIFlashcardService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public List<FlashcardDTO> generateFlashcards(String text, int numFlashcards) {
        String prompt = """
            You are a flashcard creator. Take in the following text and create exactly %d flashcards.
            Each flashcard should have a front and back, each one sentence. The front should be the term/topic and the  back should be the definition.
            They  should be high quality for advanced students not noobs.
            Format your response as:
            Front: ...
            Back: ...
        """.formatted(numFlashcards) + "\nText: " + text;


        String generatedText = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        List<FlashcardDTO> flashcards = new ArrayList<>();
        String front = null, back = null;

        for (String line : generatedText.split("\\r?\\n")) {
            line = line.trim();
            if (line.startsWith("Front:")) front = line.substring(6).trim();
            if (line.startsWith("Back:")) back = line.substring(5).trim();

            if (front != null && back != null) {
                flashcards.add(new FlashcardDTO(front, back));
                front = null;
                back = null;
            }
        }

        return flashcards;
    }
}