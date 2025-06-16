package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.GoalTransactionRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.request.GoalRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.GoalResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.IGoalRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.IGoalResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.IGoalServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.model.dto.GoalTransactionsResponse;
import com.fyrdev.monyia.domain.model.dto.LoanTransactionsResponse;
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
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {
    private final IGoalServicePort goalServicePort;
    private final IGoalRequestMapper goalRequestMapper;
    private final IGoalResponseMapper goalResponseMapper;
    private final ITransactionServicePort transactionServicePort;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<GoalResponse>> createNewGoal(
            @Valid
            @RequestBody
            GoalRequest goalRequest,
            HttpServletRequest request) {
        var goal = goalRequestMapper.toGoal(goalRequest);
        GoalResponse createdGoal = goalResponseMapper.toGoalResponse(goalServicePort.createNewGoal(goal));

        ApiResponse<GoalResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Goal created successfully",
                createdGoal,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getAllGoalsByUserId(HttpServletRequest request) {
        var goals = goalServicePort.getAllGoalsByUserId();
        var goalResponses = goalResponseMapper.toGoalResponseList(goals);

        ApiResponse<List<GoalResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Goals retrieved successfully",
                goalResponses,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<GoalResponse>> getGoalById(
            @PathVariable Long goalId,
            HttpServletRequest request) {
        var goal = goalServicePort.getGoalById(goalId);
        var goalResponse = goalResponseMapper.toGoalResponse(goal);

        ApiResponse<GoalResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Goal retrieved successfully",
                goalResponse,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> makeDeposit(
            @RequestBody GoalTransactionRequest goalTransactionRequest,
            HttpServletRequest request) {
        Long goalId = goalTransactionRequest.goalId();
        BigDecimal amount = goalTransactionRequest.amount();
        var type = goalTransactionRequest.type();

        goalServicePort.makeDepositOrWithdraw(goalId, amount, type);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Deposit made successfully",
                null,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> makeWithdrawal(
            @RequestBody GoalTransactionRequest goalTransactionRequest,
            HttpServletRequest request) {
        Long goalId = goalTransactionRequest.goalId();
        BigDecimal amount = goalTransactionRequest.amount();
        var type = goalTransactionRequest.type();

        goalServicePort.makeDepositOrWithdraw(goalId, amount, type);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Deposit made successfully",
                null,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId}/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<GoalTransactionsResponse>>> getTransactionsByLoanId(
            @PathVariable Long goalId,
            HttpServletRequest request) {
        List<GoalTransactionsResponse> transactions = transactionServicePort.findAllTransactionsByGoalId(goalId);

        ApiResponse<List<GoalTransactionsResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                transactions,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

}
