package org.example.baitap.controller;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.baitap.dto.request.LikeMovieRequest;
import org.example.baitap.dto.response.LikeMovieResponse;
import org.example.baitap.service.MovieLikeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieLikeController {
    private final MovieLikeService movieLikeService;

    @PostMapping("/like/movies/pessimistic")
    public ResponseEntity<LikeMovieResponse> likePessimistic(@Valid @RequestBody LikeMovieRequest request) {
        LikeMovieResponse response = movieLikeService.likePessimistic(request.getMovieId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/like/movies/optimistic")
    public ResponseEntity<LikeMovieResponse> likeOptimistic(@Valid @RequestBody LikeMovieRequest request) {
        LikeMovieResponse response = movieLikeService.likeOptimistic(request.getMovieId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // The client can retry a conflicting optimistic request. A duplicate like
    // racing with another request can also hit the database unique key.
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, OptimisticLockException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict() {
    }
}
