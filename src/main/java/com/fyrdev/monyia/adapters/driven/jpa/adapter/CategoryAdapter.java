package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.ICategoryEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.ICategoryRepository;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public void saveNewCategory(Category category) {
        categoryRepository.save(categoryEntityMapper.toEntity(category));
    }

    @Override
    public boolean isCategoryExists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
