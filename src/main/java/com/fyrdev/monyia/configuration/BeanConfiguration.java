package com.fyrdev.monyia.configuration;

import com.fyrdev.monyia.adapters.driven.jpa.adapter.EncryptionAdapter;
import com.fyrdev.monyia.adapters.driven.jpa.adapter.UserAdapter;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IUserRepository;
import com.fyrdev.monyia.domain.api.IUserServicePort;
import com.fyrdev.monyia.domain.api.usecase.UserUseCase;
import com.fyrdev.monyia.domain.spi.IEncryptionPort;
import com.fyrdev.monyia.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    //TODO: Remove this Bean after adding security config
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IEncryptionPort encryptionPort() {
        return new EncryptionAdapter(passwordEncoder());
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
