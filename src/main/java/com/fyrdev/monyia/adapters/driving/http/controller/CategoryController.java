package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.CategoryRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.CategoryResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.ICategoryRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.ICategoryResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.model.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

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
    public ResponseEntity<ApiResponse<CategoryResponse>> saveNewCategory(
            @Valid
            @RequestBody
            CategoryRequest categoryRequest,
            HttpServletRequest request) {
        Category category = categoryServicePort
                .saveNewCategory(categoryRequestMapper.toCategory(categoryRequest));
        CategoryResponse categoryResponse = categoryResponseMapper.toCategoryResponse(category);

        ApiResponse<CategoryResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                null,
                categoryResponse,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(HttpServletRequest request) {
        var categories = categoryServicePort.getAllCategories();
        List<CategoryResponse> categoryResponses = categoryResponseMapper.toCategoryResponses(categories);

        ApiResponse<List<CategoryResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                categoryResponses,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
