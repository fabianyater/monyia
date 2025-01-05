package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
