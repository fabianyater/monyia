package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}