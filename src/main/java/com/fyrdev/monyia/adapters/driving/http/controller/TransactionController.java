package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.TransactionRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionServicePort transactionServicePort;
    private final ICategoryServicePort categoryServicePort;
    private final ITransactionRequestMapper transactionRequestMapper;
    private final ITransactionResponseMapper transactionResponseMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TransactionResponse>> saveNewTransaction(
            @Valid
            @RequestBody
            TransactionRequest transactionRequest,
            HttpServletRequest request) {
        Long categoryId = categoryServicePort.getCategoryIdByName(transactionRequest.getCategory().getName());
        transactionRequest.getCategory().setCategoryId(categoryId);

        var transaction = transactionServicePort.saveNewTransaction(transactionRequestMapper.toTransaction(transactionRequest));
        TransactionResponse transactionResponse = transactionResponseMapper.toTransactionResponse(transaction);

        ApiResponse<TransactionResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                null,
                transactionResponse,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/monthly-income")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BigDecimal>> getMonthlyIncome(@RequestParam Long pocketId) {
        BigDecimal income = transactionServicePort.getMonthlyIncome(pocketId);
        ApiResponse<BigDecimal> response = new ApiResponse<>(
                200,
                "Ingresos del mes actual obtenidos correctamente",
                income,
                currentUrl(),
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-expense")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BigDecimal>> getMonthlyExpense(@RequestParam Long pocketId) {
        BigDecimal expense = transactionServicePort.getMonthlyExpense(pocketId);
        ApiResponse<BigDecimal> response = new ApiResponse<>(
                200,
                "Gastos del mes actual obtenidos correctamente",
                expense,
                currentUrl(),
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TransactionSummaryByCategoriesResponse>>> getTransactionSummaryByCategories(
            @RequestParam Long pocketId,
            @RequestParam TransactionType transactionType,
            HttpServletRequest request) {
        var transactionSummary = transactionServicePort.getTransactionSummaryByCategories(pocketId, transactionType);
        ApiResponse<List<TransactionSummaryByCategoriesResponse>> response = new ApiResponse<>(
                200,
                "Resumen de transacciones por categorías obtenido correctamente",
                transactionSummary,
                request.getRequestURI(),
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByCategoryName(
            HttpServletRequest request,
            @RequestParam Long pocketId,
            @RequestParam String categoryName,
            @RequestParam TransactionType type) {
        var result = transactionServicePort.listTransactionsByCategory(pocketId, type.name(), categoryName);
        var transactions = transactionResponseMapper
                .toTransactionResponseList(result);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                transactions,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    private String currentUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }


}
