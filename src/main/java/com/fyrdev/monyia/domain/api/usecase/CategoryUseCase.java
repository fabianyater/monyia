package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.exception.RequiredParamException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ICategoryPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.util.Comparator;
import java.util.List;

public class CategoryUseCase implements ICategoryServicePort {
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final AITextClassifierPort aiTextClassifierPort;

    public CategoryUseCase(ICategoryPersistencePort categoryPersistencePort, IAuthenticationPort authenticationPort, AITextClassifierPort aiTextClassifierPort) {
        this.categoryPersistencePort = categoryPersistencePort;
        this.authenticationPort = authenticationPort;
        this.aiTextClassifierPort = aiTextClassifierPort;
    }

    @Override
    public Category saveNewCategory(Category category) {
        category.setUserId(authenticationPort.getAuthenticatedUserId());
        List<String> emojis = aiTextClassifierPort.suggestEmojis(category.getName());
        category.setEmojis(emojis);
        category.setDefaultEmoji(emojis.get(0));

        return categoryPersistencePort.saveNewCategory(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryPersistencePort
                .getAllCategories(authenticationPort.getAuthenticatedUserId())
                .stream().sorted(Comparator.comparing(Category::getId).reversed())
                .toList();
    }

    @Override
    public Long getCategoryIdByName(String category) {
        if (category == null || category.isBlank()) {
            throw new RequiredParamException(DomainConstants.REQUIRED_PARAM_MESSAGE);
        }

        Long userId = authenticationPort.getAuthenticatedUserId();
        Category foundCategory = categoryPersistencePort.getCategoryByName(category, userId);

        if (foundCategory == null) {
            Category newCategory = new Category();
            newCategory.setName(category);

            return saveNewCategory(newCategory).getId();
        }

        return foundCategory.getId();
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = categoryPersistencePort.getCategoryByName(name, authenticationPort.getAuthenticatedUserId());

        if (category == null) {
            Category newCategory = new Category();
            newCategory.setName(name);

            return saveNewCategory(newCategory);
        }

        return category;
    }

    @Override
    public void updateDefaultEmoji(String categoryName, String newEmoji) {
        Category category = categoryPersistencePort.getCategoryByName(categoryName, authenticationPort.getAuthenticatedUserId());

        if (category != null) {
            categoryPersistencePort.updateDefaultEmoji(categoryName, newEmoji);
        } else {
            throw new ResourceNotFoundException(DomainConstants.CATEGORY_NOT_FOUND_MESSAGE);
        }

    }
}
