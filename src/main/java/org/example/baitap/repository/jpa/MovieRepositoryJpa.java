package org.example.baitap.repository.jpa;

import org.example.baitap.entity.MovieEntity;
import org.example.baitap.model.enums.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepositoryJpa extends JpaRepository<MovieEntity, UUID> {
    Optional<MovieEntity> findByName(String name);
}
