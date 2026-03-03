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
    private final com.example.Nhom8.repository.UserRepository userRepository;
    private final com.example.Nhom8.repository.GenreRepository genreRepository;

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
        // Find existing story by slug to prevent duplicates
        if (story.getSlug() != null) {
            java.util.Optional<Story> existing = storyService.getBySlug(story.getSlug());
            if (existing.isPresent()) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        Story createdStory = storyService.createStory(story);
        return ResponseEntity.ok(StoryDTO.fromEntity(createdStory));
    }

    @PostMapping("/import")
    public ResponseEntity<StoryDTO> importStory(@RequestBody java.util.Map<String, Object> data) {
        String slug = (String) data.get("slug");
        if (slug == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if already exists
        if (storyService.getBySlug(slug).isPresent()) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).build();
        }

        Story story = new Story();
        story.setTitle((String) data.get("title"));
        story.setSlug(slug);
        story.setAuthor((String) data.get("author"));
        story.setDescription((String) data.get("description"));
        story.setCoverImage((String) data.get("coverImage"));
        story.setStatus(Story.StoryStatus.valueOf((String) data.get("status")));
        story.setPremium((boolean) data.get("isPremium"));
        story.setViewCount(0L);

        // Handle genres
        java.util.List<String> genreNames = (java.util.List<String>) data.get("genres");
        if (genreNames != null) {
            java.util.Set<com.example.Nhom8.models.Genre> genres = new java.util.HashSet<>();
            for (String gName : genreNames) {
                com.example.Nhom8.models.Genre genre = genreRepository.findByName(gName)
                        .orElseGet(() -> {
                            com.example.Nhom8.models.Genre newG = new com.example.Nhom8.models.Genre();
                            newG.setName(gName);
                            newG.setSlug(com.example.Nhom8.utils.SlugUtils.toSlug(gName)); // Need helper
                            return genreRepository.save(newG);
                        });
                genres.add(genre);
            }
            story.setGenres(genres);
        }

        // Set creator from current auth
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            userRepository.findByUsername(username).ifPresent(story::setCreator);
        }

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
