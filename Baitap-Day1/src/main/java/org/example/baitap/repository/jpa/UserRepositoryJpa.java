package org.example.baitap.repository.jpa;

import java.util.UUID;

import org.example.baitap.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, UUID> {
}
