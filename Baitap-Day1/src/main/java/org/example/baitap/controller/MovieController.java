package org.example.baitap.controller;

import lombok.RequiredArgsConstructor;
import org.example.baitap.dto.request.CreateMovieRequest;
import org.example.baitap.dto.request.DeleteMovieRequest;
import org.example.baitap.dto.response.MovieResponse;
import org.example.baitap.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieSerivce;

    @PostMapping("/create/movies")
    public ResponseEntity<MovieResponse> createMovie(CreateMovieRequest request) {
        MovieResponse response = movieSerivce.saveMovie(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/getAll/movies")
    public ResponseEntity<List<MovieResponse>> getAllMovie() {
        List<MovieResponse> response = movieSerivce.getAllMovie();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update/movies")
    public ResponseEntity<MovieResponse> updateMovie(CreateMovieRequest request) {
        MovieResponse response = movieSerivce.updateMovie(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/delete/movies")
    public ResponseEntity<Void> deleteMovie(DeleteMovieRequest request) {
        movieSerivce.deleteMovie(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
