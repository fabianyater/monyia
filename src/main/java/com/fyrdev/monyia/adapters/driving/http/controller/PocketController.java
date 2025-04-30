package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.PocketRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.PocketResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TotaBalanceResponse;
import com.fyrdev.monyia.adapters.driving.http.mapper.IPocketRequestMapper;
import com.fyrdev.monyia.adapters.driving.http.mapper.IPocketResponseMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.model.Pocket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<ApiResponse<Double>> getBalance(@PathVariable("pocketId") Long pocketId, HttpServletRequest request) {
        Double balance = pocketServicePort.getBalance(pocketId);

        ApiResponse<Double> response = new ApiResponse<>(
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
}
