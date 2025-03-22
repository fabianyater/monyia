package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.UserRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.IUserRequestMapper;
import com.fyrdev.monyia.configuration.exceptionhandler.ApiResponse;
import com.fyrdev.monyia.domain.api.IUserServicePort;
import com.fyrdev.monyia.domain.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.fyrdev.monyia.domain.util.DomainConstants.USER_ID;
import static com.fyrdev.monyia.domain.util.DomainConstants.USER_SUCCESSFULLY_REGISTERED_MESSAGE;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Map<String, Object>>> saveNewUser(@Valid @RequestBody UserRequest userRequest, HttpServletRequest request) {
        var user = userRequestMapper.toUser(userRequest);
        User savedUser = userServicePort.saveNewUser(user);

        Map<String, Object> userData = new HashMap<>();
        userData.put(USER_ID, savedUser.getId());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                USER_SUCCESSFULLY_REGISTERED_MESSAGE,
                userData,
                request.getRequestURI() + savedUser.getId(),
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
