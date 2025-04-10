package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Category;

import java.util.List;

public interface ICategoryServicePort {
    Category saveNewCategory(Category category);
    List<Category> getAllCategories();
    Long getCategoryIdByName(String category);
}
