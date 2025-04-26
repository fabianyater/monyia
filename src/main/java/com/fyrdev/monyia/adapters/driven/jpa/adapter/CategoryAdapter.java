package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.CategoryEntity;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.ICategoryEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ICategoryRepository;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Category saveNewCategory(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);

        categoryRepository.save(categoryEntity);

        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    public List<Category> getAllCategories(Long userId) {
        List<CategoryEntity> categoryEntities = categoryRepository.findByUserEntity_Id(userId);

        return categoryEntityMapper.toCategoriesList(categoryEntities);
    }

    @Override
    public boolean isCategoryExists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public Category getCategoryByIdAndUser(Long categoryId, Long userId) {
        return categoryRepository.findByIdAndUserEntity_Id(categoryId, userId)
                .map(categoryEntityMapper::toCategory)
                .orElse(null);
    }

    @Override
    public Category getCategoryByName(String category, Long userId) {
        return categoryRepository.findByNameAndUserEntity_Id(category, userId)
                .map(categoryEntityMapper::toCategory)
                .orElse(null);
    }

    @Override
    public void updateDefaultEmoji(String categoryName, String newEmoji) {
        categoryRepository.updateDefaultEmojiByName(newEmoji, categoryName);
    }
}
