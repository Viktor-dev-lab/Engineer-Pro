package org.example.baitap.repository.jpa;

import java.util.UUID;

import org.example.baitap.entity.MovieLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieLikeRepositoryJpa extends JpaRepository<MovieLikeEntity, UUID> {
    boolean existsByUserEntityIdAndMovieEntityId(UUID userId, UUID movieId);
}
