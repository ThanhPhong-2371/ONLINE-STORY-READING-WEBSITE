package com.example.Nhom8.controllers;

import com.example.Nhom8.models.Genre;
import com.example.Nhom8.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Nhom8.utils.SlugUtils;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreRepository genreRepository;

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        return genreRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Genre> getGenreBySlug(@PathVariable String slug) {
        return genreRepository.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        if (genre.getSlug() == null || genre.getSlug().isEmpty()) {
            genre.setSlug(SlugUtils.toSlug(genre.getName()));
        }
        return ResponseEntity.ok(genreRepository.save(genre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genreDetails) {
        return genreRepository.findById(id)
                .map(genre -> {
                    genre.setName(genreDetails.getName());
                    genre.setDescription(genreDetails.getDescription());
                    genre.setSlug(SlugUtils.toSlug(genreDetails.getName()));
                    return ResponseEntity.ok(genreRepository.save(genre));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
