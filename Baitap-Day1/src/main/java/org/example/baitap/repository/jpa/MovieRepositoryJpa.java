package org.example.baitap.repository.jpa;

import org.example.baitap.entity.MovieEntity;
import org.example.baitap.model.enums.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepositoryJpa extends JpaRepository<MovieEntity, UUID> {
    Optional<MovieEntity> findByName(String name);
    Optional<MovieEntity> findByNameAndMovieStatus(String name, MovieStatus movieStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from MovieEntity m where m.id = :id")
    Optional<MovieEntity> findByIdForUpdate(@Param("id") UUID id);
}
