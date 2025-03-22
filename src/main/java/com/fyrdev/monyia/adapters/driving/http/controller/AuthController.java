package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.AuthenticationRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.AuthenticationResponse;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.IAuthenticationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.fyrdev.monyia.domain.util.DomainConstants.USER_SUCCESSFULLY_LOGGED_MESSAGE;
import static com.fyrdev.monyia.domain.util.DomainConstants.USER_SUCCESSFULLY_REGISTERED_MESSAGE;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthenticationServicePort authenticationServicePort;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        String token = authenticationServicePort.authenticate(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        );

        AuthenticationResponse authenticationResponse = AuthenticationResponse
                .builder()
                .accessToken(token)
                .build();

        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                USER_SUCCESSFULLY_LOGGED_MESSAGE,
                authenticationResponse,
                null,
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
