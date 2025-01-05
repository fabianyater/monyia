package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Category;

public interface ICategoryServicePort {
    void saveNewCategory(Category category);
}
