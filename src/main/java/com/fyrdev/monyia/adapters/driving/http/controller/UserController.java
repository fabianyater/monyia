package com.fyrdev.monyia.adapters.driving.http.controller;

import com.fyrdev.monyia.adapters.driving.http.dto.request.UserRequest;
import com.fyrdev.monyia.adapters.driving.http.mapper.IUserRequestMapper;
import com.fyrdev.monyia.domain.api.IUserServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(UserController.API_V_1_USERS)
@RequiredArgsConstructor
public class UserController {
    protected static final String API_V_1_USERS = "/api/v1/users";
    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;

    @PostMapping
    public ResponseEntity<Void> saveNewUser(@Valid @RequestBody UserRequest userRequest) {
        var user = userRequestMapper.toUser(userRequest);
        userServicePort.saveNewUser(user);

        URI location = URI.create(API_V_1_USERS.concat("/") + user.getId());

        return ResponseEntity.created(location).build();
    }
}
