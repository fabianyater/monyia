package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;

public class CategoryUseCase implements ICategoryServicePort {
    private final ICategoryPersistencePort categoryPersistencePort;

    public CategoryUseCase(ICategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void saveNewCategory(Category category) {
        categoryPersistencePort.saveNewCategory(category);
    }
}
