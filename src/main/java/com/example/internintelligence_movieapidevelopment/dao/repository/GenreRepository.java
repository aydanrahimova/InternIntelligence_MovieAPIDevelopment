package com.example.internintelligence_movieapidevelopment.dao.repository;

import com.example.internintelligence_movieapidevelopment.dao.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findById(Long id);


    Optional<Genre> findByTmdbId(Long tmdbId);

    List<Genre> findAll();

    boolean existsByNameIgnoreCase(String name);

    void deleteById(Long id);

    List<Genre> findAllById(Iterable<Long> ids);

}
