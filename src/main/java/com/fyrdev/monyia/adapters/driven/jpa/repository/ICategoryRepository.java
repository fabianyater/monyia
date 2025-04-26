package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUserEntity_Id(Long id);
    Optional<CategoryEntity> findByIdAndUserEntity_Id(Long categoryId, Long userId);

    CategoryEntity findByName(String name);

    Optional<CategoryEntity> findByNameAndUserEntity_Id(String name, Long id);

    @Transactional
    @Modifying
    @Query("update CategoryEntity c set c.defaultEmoji = ?1 where c.name = ?2")
    void updateDefaultEmojiByName(String defaultEmoji, String name);
}