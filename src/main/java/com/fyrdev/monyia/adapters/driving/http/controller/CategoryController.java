package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.CategoryRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.CategoryResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.ICategoryRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.ICategoryResponseMapper;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryServicePort categoryServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;
    private final ICategoryResponseMapper categoryResponseMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> saveNewCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        var category = categoryRequestMapper.toCategory(categoryRequest);
        categoryServicePort.saveNewCategory(category);

        URI location = URI.create("/api/v1/categories/" + category.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        var categories = categoryServicePort.getAllCategories();
        List<CategoryResponse> categoryResponses = categoryResponseMapper.toCategoryResponses(categories);

        return ResponseEntity.ok(categoryResponses);
    }
}
