package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.PocketRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.request.TransferRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.request.UpdatePocketRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.PocketResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TotaBalanceResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.IPocketRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.IPocketResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.dto.PocketBalanceSummary;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pockets")
@RequiredArgsConstructor
public class PocketController {
    private final IPocketServicePort pocketServicePort;
    private final IPocketRequestMapper pocketRequestMapper;
    private final IPocketResponseMapper pocketResponseMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> saveNewPocket(@Valid @RequestBody PocketRequest pocketRequest) {
        var pocket = pocketRequestMapper.toPocket(pocketRequest);
        pocketServicePort.saveNewPocket(pocket);

        return ResponseEntity.created(URI.create("/api/v1/pockets/" + pocket.getId())).build();
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PocketResponse>>> getPockets(
            HttpServletRequest request) {
        List<Pocket> pockets = pocketServicePort.getPockets();

        ApiResponse<List<PocketResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                pockets.stream().map(pocketResponseMapper::toPocketResponse).toList(),
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{pocketId}/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PocketBalanceSummary>> getBalance(
            @PathVariable("pocketId") Long pocketId,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            HttpServletRequest request) {
        PocketBalanceSummary balance = pocketServicePort.getBalance(pocketId, startDate);

        ApiResponse<PocketBalanceSummary> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                balance,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TotaBalanceResponse>> getTotalBalanceByUserId(HttpServletRequest request) {
        Double balance = pocketServicePort.getTotalBalanceByUserId();
        TotaBalanceResponse totalBalanceResponse = new TotaBalanceResponse(balance);

        ApiResponse<TotaBalanceResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                totalBalanceResponse,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> transferBetweenPockets(
            @Valid
            @RequestBody
            TransferRequest transferRequest,
            HttpServletRequest request) {
        Long fromPocketId = transferRequest.fromPocketId();
        Long toPocketId = transferRequest.toPocketId();
        BigDecimal amount = transferRequest.amount();

        pocketServicePort.transferBetweenPockets(fromPocketId, toPocketId, amount);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                null,
                request.getRequestURI(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{pocketId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updatePocket(
            @PathVariable("pocketId") Long pocketId,
            @Valid @RequestBody UpdatePocketRequest updatePocketRequest) {
        var pocket = pocketRequestMapper.toPocket(updatePocketRequest);
        pocketServicePort.updatePocketById(pocketId, pocket);

        return ResponseEntity.created(URI.create("/api/v1/pockets/" + pocket.getId())).build();
    }
}
