package org.example.baitap.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(
    name = "movie_likes",
    uniqueConstraints = {
        @UniqueConstraint (
            name = "user_like_movies",
            columnNames = {"user_id", "movie_id"}
        )
    }
)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieLikeEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movieEntity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public MovieLikeEntity(UserEntity userEntity, MovieEntity movieEntity){
        this.userEntity = userEntity;
        this.movieEntity = movieEntity;
        this.createdAt = LocalDateTime.now();
    }
}
