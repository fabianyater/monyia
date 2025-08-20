package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IBudgetRepository extends JpaRepository<BudgetEntity, Long> {
    Optional<BudgetEntity> findByCategoryEntity_IdAndUserEntity_Id(Long id, Long userId);

    List<BudgetEntity> findByUserEntity_Id(Long id);

    Optional<BudgetEntity> findByIdAndUserEntity_Id(Long id, Long id1);
}