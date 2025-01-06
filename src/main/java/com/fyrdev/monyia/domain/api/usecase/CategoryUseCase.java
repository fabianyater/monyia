package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;

import java.util.List;

public class CategoryUseCase implements ICategoryServicePort {
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IAuthenticationPort authenticationPort;

    public CategoryUseCase(ICategoryPersistencePort categoryPersistencePort, IAuthenticationPort authenticationPort) {
        this.categoryPersistencePort = categoryPersistencePort;
        this.authenticationPort = authenticationPort;
    }

    @Override
    public void saveNewCategory(Category category) {
        category.setUserId(authenticationPort.getAuthenticatedUserId());

        categoryPersistencePort.saveNewCategory(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryPersistencePort.getAllCategories(authenticationPort.getAuthenticatedUserId());
    }
}
