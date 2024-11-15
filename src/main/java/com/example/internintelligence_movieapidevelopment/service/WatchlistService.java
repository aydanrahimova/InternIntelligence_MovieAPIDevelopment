package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dao.entity.Watchlist;
import com.example.internintelligence_movieapidevelopment.dao.entity.WatchlistMovie;
import com.example.internintelligence_movieapidevelopment.dao.repository.MovieRepository;
import com.example.internintelligence_movieapidevelopment.dao.repository.UserRepository;
import com.example.internintelligence_movieapidevelopment.dao.repository.WatchlistMovieRepository;
import com.example.internintelligence_movieapidevelopment.dao.repository.WatchlistRepository;
import com.example.internintelligence_movieapidevelopment.dto.response.MovieOverviewDto;
import com.example.internintelligence_movieapidevelopment.dto.response.WatchlistResponseDto;
import com.example.internintelligence_movieapidevelopment.exception.AlreadyExistException;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistMovieRepository watchlistMovieRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;


    public void addToWatchlist(Long userId, Long movieId) {
        log.info("Attempting to add movie ID '{}' to user ID '{}' watchlist", movieId, userId);

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> {
            log.error("Failed to add movie to watchlist: movie ID '{}' not found", movieId);
            return new ResourceNotFound("MOVIE_NOT_FOUND");
        });

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Failed to add movie to watchlist: user ID '{}' not found", userId);
            return new ResourceNotFound("USER_NOT_FOUND");
        });

        Watchlist watchlist = user.getWatchlist();

        if (watchlistMovieRepository.existsByWatchlistAndMovie(watchlist, movie)) {
            log.error("Movie already exists in the watchlist.");
            throw new AlreadyExistException("MOVIE_ALREADY_EXISTS");
        }

        // Create and save the WatchlistMovie entity to establish the link
        WatchlistMovie watchlistMovie = new WatchlistMovie(null, watchlist, movie, LocalDateTime.now());
        watchlistMovieRepository.save(watchlistMovie);
        System.out.println(movie.getId());

        log.info("Successfully added movie ID '{}' to user ID '{}' watchlist", movieId, userId);
    }




//    public WatchlistResponseDto getWatchlist(Long userId) {
//        log.info("Attempting to get user's ID '{}' watchlist", userId);
//
//        User user = userRepository.findById(userId).orElseThrow(() -> {
//            log.error("Failed to get watchlist: user ID '{}' not found", userId);
//            return new ResourceNotFound("USER_NOT_FOUND");
//        });
//
//        Watchlist watchlist = user.getWatchlist();
//        List<WatchlistMovie> watchlistMovie = watchlistMovieRepository.findByWatchlist(watchlist);
//        List<MovieOverviewDto> movies = watchlistMovie.stream()
//                .map(wm -> new MovieOverviewDto(
//                        wm.getMovie().getTitle(),
//                        wm.getMovie().getReleaseDate()
//                ))
//                .toList();
//        WatchlistResponseDto watchlistResponseDto = new WatchlistResponseDto();
//        watchlistResponseDto.setMovies(movies);
//
//        return watchlistResponseDto;
//    }


    public Page<MovieOverviewDto> getWatchlist(Long userId, int page, int size,String sortBy,String sortDirection) {
        log.info("Attempting to get user's ID '{}' watchlist", userId);

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Failed to get watchlist: user ID '{}' not found", userId);
            return new ResourceNotFound("USER_NOT_FOUND");
        });

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);


        Pageable pageable = PageRequest.of(page, size,Sort.by(direction,sortBy));

        Page<WatchlistMovie> watchlistMoviesPage = watchlistMovieRepository.findByWatchlist(user.getWatchlist(), pageable);


        return watchlistMoviesPage.map(wm -> new MovieOverviewDto(
                wm.getMovie().getTitle(),
                wm.getMovie().getReleaseDate()
        ));
    }



    public void deleteFromWatchlist(Long userId, Long movieId) {
        log.info("Attempting delete movie ID '{}' to user ID '{}' watchlist", movieId, userId);

//        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> {
//            log.error("Failed to delete movie to watchlist: movie ID '{}' not found", movieId);
//            return new ResourceNotFound("MOVIE_NOT_FOUND");
//        });
//
//        User user = userRepository.findById(userId).orElseThrow(() -> {
//            log.error("Failed to delete movie to watchlist: user ID '{}' not found", userId);
//            return new ResourceNotFound("USER_NOT_FOUND");
//        });
//
//        Watchlist watchlist = watchlistRepository.findByUser(user)
//                .orElse(new Watchlist(null, new ArrayList<>(), user));
//
//        if (watchlist.getMovies().contains(movie)) {
//            watchlist.getMovies().remove(movie);
//            watchlistRepository.save(watchlist);
//        } else {
//            throw new IllegalArgumentException("Movie not found in the watchlist");
//        }
    }

//    public Page<WatchlistResponseDto> getWatchlist(Long userId) {
//
//    }
}
