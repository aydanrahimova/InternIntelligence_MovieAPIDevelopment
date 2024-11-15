package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.dao.entity.Genre;
import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dao.entity.Person;
import com.example.internintelligence_movieapidevelopment.dao.repository.GenreRepository;
import com.example.internintelligence_movieapidevelopment.dao.repository.MovieRepository;
import com.example.internintelligence_movieapidevelopment.dao.repository.PersonRepository;
import com.example.internintelligence_movieapidevelopment.dto.request.MovieFilterDto;
import com.example.internintelligence_movieapidevelopment.dto.request.MovieRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.MovieResponseDto;
import com.example.internintelligence_movieapidevelopment.enums.PersonRole;
import com.example.internintelligence_movieapidevelopment.exception.AlreadyExistException;
import com.example.internintelligence_movieapidevelopment.exception.IllegalArgumentException;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFound;
import com.example.internintelligence_movieapidevelopment.mapper.MovieMapper;
import com.example.internintelligence_movieapidevelopment.service.specification.MovieSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final PersonRepository personRepository;
    private final GenreRepository genreRepository;

    public MovieResponseDto getMovieById(Long id) {
        log.info("Attempting find a movie with ID '{}'", id);
        Movie movie = movieRepository.findById(id).orElseThrow(() -> {
            log.error("Movie with ID '{}' is not found", id);
            return new ResourceNotFound("MOVIE_NOT_FOUND");
        });
        MovieResponseDto responseDto = movieMapper.toDto(movie);
        log.info("Movie with ID '{}' is found and returned", id);
        return responseDto;
    }

    public Page<MovieResponseDto> getMovies(Pageable pageable, MovieFilterDto movieFilterDto) {
//        if (page < 0) page = 0;
//        if (size <= 0) size = 10; // Default to 10 if size is invalid

        //lazimdi ki??


//        if (movieFilterDto == null) {
//            Page<Movie> movies = movieRepository.findAll(pageable);
//            log.info("All movies are returned");
//            System.out.println("sdnfksfk");
//            return movies.map(movieMapper::toDto);
//        }

        Specification<Movie> specification = Specification.where(
                new MovieSpecification(movieFilterDto)
        );

        log.info("Applying filters and fetching movies");
        Page<Movie> movies = movieRepository.findAll(specification, pageable);
        log.info("Filtered movies are returned");
        return movies.map(movieMapper::toDto);
    }


    public MovieResponseDto addMovie(MovieRequestDto movieRequestDto) {
//        if(movieRequestDto==null){
//            log.warn("Can't add null movie");
//            throw new IllegalArgumentException("Movie can't be null");
//        }//lazimdi?
        //onsuz butun fieldlara notNul qoymusan
        if (movieRepository.existsByTitleAndReleaseDate(movieRequestDto.getTitle(), movieRequestDto.getReleaseDate())) {
            log.warn("Movie with '{}' title name and '{}' release date is already exist", movieRequestDto.getTitle(), movieRequestDto.getReleaseDate());
            throw new AlreadyExistException("This movie is already exist");
        }
        Movie movie = movieMapper.toEntity(movieRequestDto);
        List<Person> cast = personRepository.findAllById(movieRequestDto.getPeopleId());
        List<Genre> genres = genreRepository.findAllById(movieRequestDto.getGenresId());
        movie.setCast(cast);
        movie.setGenres(genres);
        if (movie.getCast().stream().noneMatch(person -> person.getRole() == PersonRole.DIRECTOR)) {//bunu yoxla equals yoxsa ==
            log.warn("Movie with '{}' title name don't have a director", movieRequestDto.getTitle());
            throw new IllegalArgumentException("Movie without director can't exist");
        }
        movieRepository.save(movie);
        log.info("Movie with title '{}' is successfully added", movieRequestDto.getTitle());
        return movieMapper.toDto(movie);
    }

    public void deleteMovie(Long id) {
        log.info("Attempting delete a movie with ID '{}'", id);
        if (!movieRepository.existsById(id)) {
            log.error("Failed to delete movie. Movie ID '{}' doesn't exists.", id);
            throw new ResourceNotFound("MOVIE_NOT_FOUND");
        }
        movieRepository.deleteById(id);
        log.info("Movie with {} id is successfully deleted", id);
    }

    public MovieResponseDto updateMovie(Long id, MovieRequestDto movieRequestDto) {
        log.info("Attempting to update a movie with ID '{}'", id);

        Movie movie = movieRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to update movie: movie ID '{}' doesn't exists.", id);
            return new ResourceNotFound("MOVIE_NOT_FOUND");
        });
        movieMapper.mapForUpdate(movie, movieRequestDto);

        Set<Long> peopleIds = movieRequestDto.getPeopleId();
        List<Person> cast = personRepository.findAllById(peopleIds);
        if (cast.size() != peopleIds.size()) {
            List<Long> invalidIds = peopleIds.stream()
                    .filter(personId -> cast.stream().noneMatch(person -> person.getId().equals(personId)))
                    .collect(Collectors.toList());
            log.error("Failed to update movie: Some people IDs are invalid: {}", invalidIds);
            throw new ResourceNotFound("PEOPLE_NOT_FOUND");
        }
        if(cast.stream().noneMatch(person->person.getRole().equals(PersonRole.DIRECTOR))){
            log.warn("Failed to update movie ID '{}': title name don't have a director",id);
            throw new IllegalArgumentException("Movie without director can't exist");
        }
        movie.setCast(cast);

        Set<Long> genreIds = movieRequestDto.getGenresId();
        List<Genre> genres = genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) {
            List<Long> invalidIds = genreIds.stream()
                    .filter(genreId -> genres.stream().noneMatch(genre -> genre.getId().equals(genreId)))
                    .collect(Collectors.toList());
            log.error("Failed to update movie: Some genre IDs are invalid: {}", invalidIds);
            throw new ResourceNotFound("GENRE_NOT_FOUND");
        }
        movie.setGenres(genres);

        movieRepository.save(movie);
        log.info("Movie is successfully updated");
        return movieMapper.toDto(movie);
    }

}
