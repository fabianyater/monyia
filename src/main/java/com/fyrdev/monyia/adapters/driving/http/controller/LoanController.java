package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.LoanPaymentRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.request.LoanRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.LoanDetailResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.LoanResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.ILoanRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.ILoanResponseMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.ILoanServicePort;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.model.dto.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.TotalBalanceLoanedResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final ILoanServicePort loanServicePort;
    private final IPocketServicePort pocketServicePort;
    private final ITransactionServicePort transactionServicePort;
    private final ILoanRequestMapper loanRequestMapper;
    private final ILoanResponseMapper loanResponseMapper;
    private final ITransactionResponseMapper transactionResponseMapper;

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

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getAllLoans(HttpServletRequest request) {
        var loans = loanServicePort.getLoansByPocketId();

        List<LoanResponse> loanResponseList = loanResponseMapper.toLoanResponseList(loans);


        ApiResponse<List<LoanResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Bolsillo creado correctamente",
                loanResponseList,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{loanId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<LoanDetailResponse>> getLoanDetails(
            @PathVariable("loanId") Long loanId,
            HttpServletRequest request) {
        var loan = loanServicePort.getLoanDetails(loanId);
        var pocket = pocketServicePort.getPocketByIdAndUserId(loan.getPocketId());

        LoanDetailResponse loanResponse = new LoanDetailResponse(
                loan.getId(),
                loan.getLoanParty(),
                loan.getDescription(),
                loan.getAmount().doubleValue(),
                loan.getBalance().doubleValue(),
                loan.getStartDate(),
                loan.getLoanType().name(),
                pocket.getName()
        );

        ApiResponse<LoanDetailResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Detalles del préstamo obtenidos correctamente",
                loanResponse,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{loanId}/type/{loanType}/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanTransactionsResponse>>> getTransactionsByLoanId(
            @PathVariable Long loanId,
            @PathVariable String loanType,
            HttpServletRequest request) {
        List<LoanTransactionsResponse> transactions = transactionServicePort.findAllTransactionsByLoanId(loanId, loanType);

        ApiResponse<List<LoanTransactionsResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                transactions,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> makePayment(
            @Valid
            @RequestBody
            LoanPaymentRequest loanPaymentRequest,
            HttpServletRequest request) {
        Long loanId = loanPaymentRequest.loanId();
        Long pocketId = loanPaymentRequest.pocketId();
        BigDecimal amount = loanPaymentRequest.amount();

        loanServicePort.makePayment(loanId, pocketId, amount);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Pago realizado correctamente",
                null,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-loaned")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TotalBalanceLoanedResponse>> getTotalLoanedBalance(
            HttpServletRequest request) {
        Double balance = loanServicePort.totalLoaned();

        TotalBalanceLoanedResponse totalBalanceLoanedResponse = new TotalBalanceLoanedResponse(balance);

        ApiResponse<TotalBalanceLoanedResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                totalBalanceLoanedResponse,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }
}
