package com.fyrdev.monyia.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotNull(message = "Category id cannot be null")
    @NotBlank(message = "Category name cannot be empty")
    private String name;

    @NotNull(message = "Category emoji cannot be null")
    @NotBlank(message = "Category emoji cannot be empty")
    private String emoji;
}
