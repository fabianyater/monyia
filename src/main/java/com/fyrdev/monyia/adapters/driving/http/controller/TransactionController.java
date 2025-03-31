package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.TransactionRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionRequestMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionServicePort transactionServicePort;
    private final ITransactionRequestMapper transactionRequestMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> saveNewTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        var transaction = transactionRequestMapper.toTransaction(transactionRequest);
        transactionServicePort.saveNewTransaction(transaction);

        return ResponseEntity.created(URI.create("/api/v1/transactions/" + transaction.getId())).build();
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

    @GetMapping("/monthly-total")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BigDecimal>> getMonthlyTotal(@RequestParam Long pocketId) {
        BigDecimal total = transactionServicePort.getMonthlyTotal(pocketId);
        ApiResponse<BigDecimal> response = new ApiResponse<>(
                200,
                "Balance neto del mes actual obtenido correctamente",
                total,
                currentUrl(),
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    private String currentUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }


}
