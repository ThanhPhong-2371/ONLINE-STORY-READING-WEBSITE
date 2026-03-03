package com.example.Nhom8.service;

import com.example.Nhom8.models.Genre;
import com.example.Nhom8.models.Story;
import com.example.Nhom8.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;

    public Page<Story> getAllStories(Pageable pageable) {
        return storyRepository.findAll(pageable);
    }

    public Page<Story> searchStories(String keyword, Pageable pageable) {
        return storyRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public Page<Story> filterByGenres(List<Genre> genres, Pageable pageable) {
        return storyRepository.findByGenresIn(genres, pageable);
    }

    public Story getStoryById(Long id) {
        return storyRepository.findById(id).orElseThrow(() -> new RuntimeException("Story not found"));
    }

    public Story createStory(Story story) {
        return storyRepository.save(story);
    }

    public Story updateStory(Long id, Story storyDetails) {
        Story story = getStoryById(id);
        story.setTitle(storyDetails.getTitle());
        story.setSlug(storyDetails.getSlug());
        story.setDescription(storyDetails.getDescription());
        story.setCoverImage(storyDetails.getCoverImage());
        story.setAuthor(storyDetails.getAuthor());
        story.setStatus(storyDetails.getStatus());
        story.setPremium(storyDetails.isPremium());
        story.setGenres(storyDetails.getGenres());
        return storyRepository.save(story);
    }

    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }
}
