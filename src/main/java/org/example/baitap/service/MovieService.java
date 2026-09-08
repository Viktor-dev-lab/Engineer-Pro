package org.example.baitap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baitap.dto.request.CreateMovieRequest;
import org.example.baitap.dto.response.MovieResponse;
import org.example.baitap.entity.MovieEntity;
import org.example.baitap.model.enums.MovieStatus;
import org.example.baitap.repository.jpa.MovieRepositoryJpa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepositoryJpa movieRepositoryJpa;

    // Save
    @Transactional
    public MovieResponse saveMovie(CreateMovieRequest request) {
        Optional<MovieEntity> existingMovie = movieRepositoryJpa.findByName(request.getName());

        if (existingMovie.isPresent()) {
            MovieEntity movie = existingMovie.get();
            // case 1 conflict
            if (movie.getMovieStatus() == MovieStatus.ACTIVE) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Movie already exists");
            }
            // Case 2 update exist
            movie.setMovieStatus(MovieStatus.ACTIVE);
            movie.setMovieType(request.getMovieType());
            movie.setName(request.getName());

            MovieEntity savedMovie = movieRepositoryJpa.save(movie);
            return toResponse(savedMovie);
        }
        // case 3 create new
        MovieEntity newMovie = toEntity(request);
        MovieEntity savedMovie = movieRepositoryJpa.save(newMovie);
        return toResponse(savedMovie);
    }

    private MovieResponse toResponse(MovieEntity entity){
        if (entity == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Movie entity cannot be null");
        }
        return MovieResponse.builder()
                .movieType(entity.getMovieType())
                .name(entity.getName())
                .build();
    }

    private MovieEntity toEntity(CreateMovieRequest request){
        return MovieEntity.builder()
                .movieStatus(MovieStatus.ACTIVE)
                .movieType(request.getMovieType())
                .name(request.getName())
                .build();
    }
}
