package com.fyrdev.monyia.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String emoji;
}
