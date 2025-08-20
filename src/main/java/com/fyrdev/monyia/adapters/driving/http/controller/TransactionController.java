package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.TransactionRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.model.dto.IncomeAndExpenseSummary;
import com.fyrdev.monyia.domain.model.dto.QueryFilters;
import com.fyrdev.monyia.domain.model.dto.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/monthly-summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<IncomeAndExpenseSummary>>> getMonthlyIncomeAndExpenseSummary(
            @RequestParam Long pocketId,
            @RequestParam LocalDateTime startDate) {
        List<IncomeAndExpenseSummary> result = transactionServicePort.getMonthlyIncomeAndExpenseSummary(pocketId, startDate);

        ApiResponse<List<IncomeAndExpenseSummary>> response = new ApiResponse<>(
                200,
                "Ingresos y gastos del mes actual obtenidos correctamente",
                result,
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
            @RequestParam LocalDateTime startDate,
            HttpServletRequest request) {
        var transactionSummary = transactionServicePort.getTransactionSummaryByCategories(pocketId, transactionType, startDate);
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
            @RequestParam TransactionType type,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false)
            LocalDateTime startDate,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false)
            LocalDateTime endDate) {
        var result = transactionServicePort.listTransactionsByCategory(pocketId, type, categoryName, startDate, endDate);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                result,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getLatestTransactions(
            HttpServletRequest request,
            @RequestParam Long pocketId) {
        var result = transactionServicePort.getLatestTransactionsByPocketId(pocketId);
        var transactions = transactionResponseMapper
                .toTransactionResponseModelList(result);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                transactions,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(
            HttpServletRequest request,
            @RequestParam Long pocketId,
            @RequestParam(required = false)
            LocalDate startMonth) {
        var result = transactionServicePort.getTransactionsByPocketId(pocketId, startMonth);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                result,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsWithFilters(
            HttpServletRequest request,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String pocket,
            @RequestParam(required = false) String text,
            @RequestParam(value = "page", defaultValue = "0")
            @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "15")
            @Min(1) Integer size,
            @RequestParam(
                    value = "order",
                    defaultValue = "DESC",
                    required = false)
            String order) {
        QueryFilters queryFilters = QueryFilters.builder()
                .category(category)
                .text(text)
                .type(type)
                .pocket(pocket)
                .build();
        var result = transactionServicePort.getAllTransactionsWithFilters(queryFilters, page, size, order);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                result,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    private String currentUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }


}
