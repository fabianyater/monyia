package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.AuthenticationRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.AuthenticationResponse;
import com.fyrdev.monyia.domain.api.IAuthenticationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthenticationServicePort authenticationServicePort;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        String token = authenticationServicePort.authenticate(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        );

        AuthenticationResponse response = AuthenticationResponse
                .builder()
                .accessToken(token)
                .build();

        return ResponseEntity.ok(response);
    }

}
