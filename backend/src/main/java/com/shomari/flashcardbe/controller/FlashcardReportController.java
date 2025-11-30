package com.shomari.flashcardbe.controller;

import com.shomari.flashcardbe.entity.FlashcardSet;
import com.shomari.flashcardbe.service.FlashcardSetService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reports")
public class FlashcardReportController {

    private final FlashcardSetService flashcardSetService;

    public FlashcardReportController(FlashcardSetService flashcardSetService) {
        this.flashcardSetService = flashcardSetService;
    }

    @GetMapping("/flashcards/{setId}")
    public void exportSetCsv(@PathVariable Long setId,
                             Authentication authentication,
                             HttpServletResponse response) throws Exception {

        String userId = authentication.getName();
        FlashcardSet set = flashcardSetService.getSetByIdAndUser(setId, userId);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + set.getName() + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Front,Back,Topic,Created At,Updated At");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        set.getFlashcards().forEach(fc -> {
            writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    fc.getQuestion(),
                    fc.getAnswer(),
                    fc.getTopic(),
                    fc.getCreatedAt().format(formatter),
                    fc.getUpdatedAt().format(formatter));
        });

        writer.flush();
        writer.close();
    }
}
