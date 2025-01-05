package com.fyrdev.monyia.configuration;

import com.fyrdev.monyia.adapters.driven.jpa.adapter.AuthenticationAdapter;
import com.fyrdev.monyia.adapters.driven.jpa.adapter.EncryptionAdapter;
import com.fyrdev.monyia.adapters.driven.jpa.adapter.UserAdapter;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IUserRepository;
import com.fyrdev.monyia.configuration.security.jwt.JwtUtils;
import com.fyrdev.monyia.domain.api.IAuthenticationServicePort;
import com.fyrdev.monyia.domain.api.IUserServicePort;
import com.fyrdev.monyia.domain.api.usecase.AuthenticationUseCase;
import com.fyrdev.monyia.domain.api.usecase.UserUseCase;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IEncryptionPort;
import com.fyrdev.monyia.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public IEncryptionPort encryptionPort() {
        return new EncryptionAdapter(passwordEncoder);
    }

    @Bean
    public IAuthenticationPort authenticationPort() {
        return new AuthenticationAdapter(authenticationManager, jwtUtils);
    }

    @Bean
    public IAuthenticationServicePort authenticationServicePort() {
        return new AuthenticationUseCase(authenticationPort());
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort(), encryptionPort());
    }
}
