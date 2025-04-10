package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.response.CategoryResponse;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.AiTextClassifierServicePort;
import com.fyrdev.monyia.domain.model.ClassificationResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/classify")
@RequiredArgsConstructor
public class AiClassifierController {
    private final AiTextClassifierServicePort textClassifierServicePort;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClassificationResult>> classifyTransaction(
            @RequestParam("prompt") String prompt,
            HttpServletRequest request) {
        ClassificationResult result = textClassifierServicePort.classifyTransaction(prompt);

        ApiResponse<ClassificationResult> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                result,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
