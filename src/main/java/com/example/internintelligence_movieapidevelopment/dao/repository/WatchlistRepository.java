package com.example.internintelligence_movieapidevelopment.dao.repository;

import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dao.entity.Watchlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchlistRepository  extends JpaRepository<Watchlist,Long> {
    Optional<Watchlist> findByUser(User user);

    boolean existsByUserAndMovie(User user, Movie movie);

    Page<Watchlist> findAllByUser(User user, Pageable pageable);
}
