package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Category;

import java.util.List;

public interface ICategoryPersistencePort {
    void saveNewCategory(Category category);
    List<Category> getAllCategories(Long userId);
    boolean isCategoryExists(Long categoryId);
    Category getCategoryByIdAndUser(Long categoryId, Long userId);
}
