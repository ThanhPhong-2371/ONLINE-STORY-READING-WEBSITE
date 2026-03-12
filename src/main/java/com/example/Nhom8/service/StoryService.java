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

    public java.util.Optional<Story> getBySlug(String slug) {
        return storyRepository.findBySlug(slug);
    }

    public Page<Story> filterByGenres(List<Genre> genres, Pageable pageable) {
        return storyRepository.findByGenresIn(genres, pageable);
    }

    public Page<Story> filterByGenreSlug(String slug, Pageable pageable) {
        return storyRepository.findByGenreSlug(slug, pageable);
    }

    public Page<Story> filterByStatus(Story.StoryStatus status, Pageable pageable) {
        return storyRepository.findByStatus(status, pageable);
    }

    public Page<Story> filterByPremium(boolean isPremium, Pageable pageable) {
        return storyRepository.findByIsPremium(isPremium, pageable);
    }

    public Story getStoryById(Long id) {
        return storyRepository.findById(id).orElseThrow(() -> 
            new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Story not found"));
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

    public List<Story> getTopRatedStories(int limit) {
        return storyRepository.findTopRatedStories(org.springframework.data.domain.PageRequest.of(0, limit));
    }
}
