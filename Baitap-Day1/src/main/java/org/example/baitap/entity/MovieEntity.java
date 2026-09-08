package org.example.baitap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.baitap.model.enums.MovieStatus;
import org.example.baitap.model.enums.MovieType;

import java.util.UUID;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private MovieStatus movieStatus;

    @Enumerated(EnumType.STRING)
    private MovieType movieType;

    @Builder
    public MovieEntity(String name, MovieStatus movieStatus, MovieType movieType){
        this.name = name;
        this.movieStatus = movieStatus;
        this.movieType = movieType;
    }
}
