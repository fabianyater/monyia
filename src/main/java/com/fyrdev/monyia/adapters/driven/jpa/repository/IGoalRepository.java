package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IGoalRepository extends JpaRepository<GoalEntity, Long> {
    boolean existsByIdAndUserEntity_Id(Long id, Long id1);

    List<GoalEntity> findByUserEntity_Id(Long id);

    @Query("select g from GoalEntity g where g.id = ?1 and g.userEntity.id = ?2")
    Optional<GoalEntity> findByIdAndUserEntity_Id(Long id, Long id1);
}