package org.example.baitap.controller;

import lombok.RequiredArgsConstructor;
import org.example.baitap.dto.request.CreateMovieRequest;
import org.example.baitap.dto.response.MovieResponse;
import org.example.baitap.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieSerivce;

    @GetMapping("/api/movies")
    public String GetListMovies(){
        return "200";
    }

    @PostMapping("/api/movie")
    public ResponseEntity<MovieResponse> createMovie(CreateMovieRequest request){
        MovieResponse response = movieSerivce.saveMovie(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
