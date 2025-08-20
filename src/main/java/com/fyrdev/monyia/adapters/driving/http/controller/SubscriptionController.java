package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.BudgetRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.request.SubscriptionRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.BudgetResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.SubscriptionResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.ISubscriptionMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ISubscriptionServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final ISubscriptionServicePort subscriptionServicePort;
    private final ISubscriptionMapper subscriptionMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> addSubscription(
            @Valid
            @RequestBody
            SubscriptionRequest subscriptionRequest,
            HttpServletRequest request) {
        var subscription = subscriptionMapper.toSubscription(subscriptionRequest);

        subscriptionServicePort.addNewSubscription(subscription);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                null,
                null,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getSubscriptions(HttpServletRequest request) {
        var subscriptions = subscriptionServicePort.getSubscriptions();

        ApiResponse<List<SubscriptionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Subscriptions retrieved successfully",
                subscriptions,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

}
