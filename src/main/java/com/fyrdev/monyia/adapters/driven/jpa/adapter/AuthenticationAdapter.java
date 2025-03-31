package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.UserEntity;
import com.fyrdev.monyia.configuration.security.jwt.JwtUtils;
import com.fyrdev.monyia.domain.model.User;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class AuthenticationAdapter implements IAuthenticationPort {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public User authenticate(String email, String password) {
        Authentication authentication = performAuthentication(email, password);
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        return new User(
                userEntity.getId(),
                userEntity.getUuid(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getColor(),
                userEntity.getPassword()
        );
    }

    @Override
    public boolean validateCredentials(String email, String password) {
        try {
            Authentication authentication = performAuthentication(email, password);
            return authentication.isAuthenticated();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateToken(User user) {
        return jwtUtils.generateToken(user);
    }

    @Override
    public Long getAuthenticatedUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        return Long.valueOf(userId);
    }

    private Authentication performAuthentication(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
    }

}
