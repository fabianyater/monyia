package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.TransactionRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.ITransactionRequestMapper;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionServicePort transactionServicePort;
    private final ITransactionRequestMapper transactionRequestMapper;

    @PostMapping("/")
    public ResponseEntity<Void> saveNewTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        var transaction = transactionRequestMapper.toTransaction(transactionRequest);
        transactionServicePort.saveNewTransaction(transaction);

        return ResponseEntity.created(URI.create("/api/v1/transactions/" + transaction.getId())).build();
    }
}
