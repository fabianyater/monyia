package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.PocketRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.IPocketRequestMapper;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/pockets")
@RequiredArgsConstructor
public class PocketController {
    private final IPocketServicePort pocketServicePort;
    private final IPocketRequestMapper pocketRequestMapper;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> saveNewPocket(@Valid @RequestBody PocketRequest pocketRequest) {
        var pocket = pocketRequestMapper.toPocket(pocketRequest);
        pocketServicePort.saveNewPocket(pocket);

        return ResponseEntity.created(URI.create("/api/v1/pockets/" + pocket.getId())).build();
    }
}
