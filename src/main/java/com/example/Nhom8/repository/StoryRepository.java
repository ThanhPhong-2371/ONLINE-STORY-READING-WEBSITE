package com.example.Nhom8.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Nhom8.models.Genre;
import com.example.Nhom8.models.Story;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Story> findByGenresIn(List<Genre> genres, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT s FROM Story s JOIN s.genres g WHERE g.slug = :slug")
    Page<Story> findByGenreSlug(@org.springframework.data.repository.query.Param("slug") String slug,
            Pageable pageable);

    java.util.Optional<Story> findBySlug(String slug);

    Page<Story> findByStatus(Story.StoryStatus status, Pageable pageable);

    Page<Story> findByIsPremium(boolean isPremium, Pageable pageable);

    // For Statistics
    List<Story> findTop10ByOrderByViewCountDesc();
}
