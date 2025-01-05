package com.fyrdev.monyia.configuration.security.jwt;

import com.fyrdev.monyia.adapters.driven.jpa.entity.UserEntity;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final String INVALID_TOKEN = "Invalid token";
    private static final String INVALID_CREDENTIALS = "Invalid credentials";
    protected static final String EMTPY_STRING = "";
    private final JwtUtils jwtUtils;
    private final IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(PREFIX, EMTPY_STRING);

        try {
            if (jwtUtils.isTokenValid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_TOKEN);
            }

            String userId = jwtUtils.getUserIdFromToken(token);
            UserEntity userEntity = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new RuntimeException(INVALID_CREDENTIALS));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userId, null, userEntity.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}
