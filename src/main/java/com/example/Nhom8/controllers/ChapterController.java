package com.example.Nhom8.controllers;

import com.example.Nhom8.models.Chapter;
import com.example.Nhom8.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/story/{storyId}")
    public ResponseEntity<Page<Chapter>> getChaptersByStory(@PathVariable Long storyId, Pageable pageable) {
        return ResponseEntity.ok(chapterService.getChaptersByStory(storyId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapterById(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.getChapterById(id));
    }

    @GetMapping("/story/{storyId}/number/{number}")
    public ResponseEntity<Chapter> getChapterByNumber(@PathVariable Long storyId, @PathVariable int number) {
        return ResponseEntity.ok(chapterService.getChapterByNumber(storyId, number));
    }
}
