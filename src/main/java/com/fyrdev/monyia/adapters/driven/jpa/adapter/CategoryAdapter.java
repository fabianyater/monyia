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
    public void saveNewCategory(Category category) {
        categoryRepository.save(categoryEntityMapper.toEntity(category));
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
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(categoryEntityMapper::toCategory)
                .orElse(null);
    }
}
