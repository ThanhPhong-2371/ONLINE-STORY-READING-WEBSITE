package com.example.Nhom8.controllers;

import com.example.Nhom8.dto.StoryDTO;
import com.example.Nhom8.models.Story;
import com.example.Nhom8.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {
    private final StoryService storyService;

    @GetMapping
    public ResponseEntity<Page<StoryDTO>> getAllStories(Pageable pageable) {
        Page<Story> stories = storyService.getAllStories(pageable);
        return ResponseEntity.ok(stories.map(StoryDTO::fromEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryDTO> getStoryById(@PathVariable Long id) {
        Story story = storyService.getStoryById(id);
        return ResponseEntity.ok(StoryDTO.fromEntity(story));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<StoryDTO>> searchStories(@RequestParam String q, Pageable pageable) {
        Page<Story> stories = storyService.searchStories(q, pageable);
        return ResponseEntity.ok(stories.map(StoryDTO::fromEntity));
    }

    @PostMapping
    public ResponseEntity<StoryDTO> createStory(@RequestBody Story story) {
        Story createdStory = storyService.createStory(story);
        return ResponseEntity.ok(StoryDTO.fromEntity(createdStory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryDTO> updateStory(@PathVariable Long id, @RequestBody Story storyDetails) {
        Story updatedStory = storyService.updateStory(id, storyDetails);
        return ResponseEntity.ok(StoryDTO.fromEntity(updatedStory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
}
