package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.CategoryRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.ICategoryRequestMapper;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryServicePort categoryServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> saveNewCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        var category = categoryRequestMapper.toCategory(categoryRequest);
        categoryServicePort.saveNewCategory(category);

        URI location = URI.create("/api/v1/categories/" + category.getId());

        return ResponseEntity.created(location).build();
    }
}
