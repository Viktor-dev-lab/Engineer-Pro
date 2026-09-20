package org.example.baitap.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.example.baitap.dto.response.LikeMovieResponse;
import org.example.baitap.entity.MovieEntity;
import org.example.baitap.entity.MovieLikeEntity;
import org.example.baitap.entity.UserEntity;
import org.example.baitap.model.enums.MovieStatus;
import org.example.baitap.repository.jpa.MovieLikeRepositoryJpa;
import org.example.baitap.repository.jpa.MovieRepositoryJpa;
import org.example.baitap.repository.jpa.UserRepositoryJpa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MovieLikeService {
    private final MovieRepositoryJpa movieRepository;
    private final MovieLikeRepositoryJpa movieLikeRepository;
    private final UserRepositoryJpa userRepository;

    // Pessimistic Lock
    @Transactional
    public LikeMovieResponse likePessimistic(UUID movieId, UUID userId) {
        MovieEntity movie = movieRepository.findByIdForUpdate(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
        return addLike(movie, userId);
    }

    // Optimistic Lock
    @Transactional
    public LikeMovieResponse likeOptimistic(UUID movieId, UUID userId) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
        return addLike(movie, userId);
    }

    private LikeMovieResponse addLike(MovieEntity movie, UUID userId) {
        // case 1: status movie active
        if (movie.getMovieStatus() != MovieStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }

        // case 2: user exist
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // case 3: protects this rule when two requests from the same user race.
        if (movieLikeRepository.existsByUserEntityIdAndMovieEntityId(userId, movie.getId())) {
            return toResponse(movie);
        }

        movie.setLikeCount(movie.getLikeCount() + 1);
        movieRepository.saveAndFlush(movie);

        movieLikeRepository.save(MovieLikeEntity.builder()
                .userEntity(user)
                .movieEntity(movie)
                .build());
        return toResponse(movie);
    }

    private LikeMovieResponse toResponse(MovieEntity movie) {
        return LikeMovieResponse.builder()
                .movieId(movie.getId())
                .likeCount(movie.getLikeCount())
                .build();
    }
}
