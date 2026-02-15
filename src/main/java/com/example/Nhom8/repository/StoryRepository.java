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

    // For Statistics
    List<Story> findTop10ByOrderByViewCountDesc();
}
