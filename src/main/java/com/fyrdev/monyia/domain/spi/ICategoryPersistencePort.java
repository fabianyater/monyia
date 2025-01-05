package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Category;

public interface ICategoryPersistencePort {
    void saveNewCategory(Category category);
    boolean isCategoryExists(Long categoryId);
}
