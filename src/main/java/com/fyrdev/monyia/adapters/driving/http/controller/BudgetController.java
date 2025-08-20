package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.BudgetRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.BudgetResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.IBudgetRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.IBudgetResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.IBudgetServicePort;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final IBudgetServicePort budgetServicePort;
    private final IBudgetRequestMapper budgetRequestMapper;
    private final IBudgetResponseMapper budgetResponseMapper;
    private final ICategoryServicePort categoryServicePort;
    private final ITransactionServicePort transactionServicePort;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> saveNewBudget(
            @Valid
            @RequestBody
            BudgetRequest budgetRequest,
            HttpServletRequest request) {
        Long categoryId = categoryServicePort.getCategoryIdByName(budgetRequest.category().getName());
        budgetRequest.category().setCategoryId(categoryId);
        budgetServicePort
                .saveBudget(budgetRequestMapper.toBudget(budgetRequest));

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
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getAllBudgets(HttpServletRequest request) {
        var budgets = budgetServicePort.getAllBudgets();

        ApiResponse<List<BudgetResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Budgets retrieved successfully",
                budgets,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{budgetId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BudgetResponse>> getBudgetById(
            HttpServletRequest request,
            @PathVariable("budgetId")
            Long budgetId) {
        var budget = budgetServicePort.getBudgetById(budgetId);

        ApiResponse<BudgetResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Budgets retrieved successfully",
                budget,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{budgetId}/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByBudgetId(
            @PathVariable Long budgetId,
            HttpServletRequest request) {
        List<TransactionResponse> transactions = transactionServicePort.findAllTransactionsByBudgetId(budgetId);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                transactions,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }
}
