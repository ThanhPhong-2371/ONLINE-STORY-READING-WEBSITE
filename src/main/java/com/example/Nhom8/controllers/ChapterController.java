package com.example.Nhom8.controllers;

import com.example.Nhom8.dto.ChapterDTO;
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
    public ResponseEntity<Page<ChapterDTO>> getChaptersByStory(@PathVariable Long storyId, Pageable pageable) {
        Page<Chapter> chapters = chapterService.getChaptersByStory(storyId, pageable);
        return ResponseEntity.ok(chapters.map(ChapterDTO::fromEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> getChapterById(@PathVariable Long id) {
        Chapter chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(ChapterDTO.fromEntity(chapter));
    }

    @GetMapping("/story/{storyId}/number/{number}")
    public ResponseEntity<ChapterDTO> getChapterByNumber(@PathVariable Long storyId, @PathVariable int number) {
        Chapter chapter = chapterService.getChapterByNumber(storyId, number);
        return ResponseEntity.ok(ChapterDTO.fromEntity(chapter));
    }

    @PostMapping
    public ResponseEntity<ChapterDTO> createChapter(@RequestBody Chapter chapter) {
        Chapter createdChapter = chapterService.createChapter(chapter);
        return ResponseEntity.ok(ChapterDTO.fromEntity(createdChapter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChapterDTO> updateChapter(@PathVariable Long id, @RequestBody Chapter chapterDetails) {
        Chapter updatedChapter = chapterService.updateChapter(id, chapterDetails);
        return ResponseEntity.ok(ChapterDTO.fromEntity(updatedChapter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}
