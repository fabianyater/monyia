package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.LoanRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.request.PocketRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.ILoanRequestMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ILoanServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final ILoanServicePort loanServicePort;
    private final ILoanRequestMapper loanRequestMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> saveNewLoan(
            @Valid
            @RequestBody
            LoanRequest loanRequest,
            HttpServletRequest request) {
        var loan = loanRequestMapper.toLoan(loanRequest);

        loanServicePort.saveLoan(loan);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Préstamo creado correctamente",
                null,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
