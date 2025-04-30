package com.fyrdev.monyia.configuration;

import com.fyrdev.monyia.adapters.driven.ai.OllamaClient;
import com.fyrdev.monyia.adapters.driven.ai.OllamaAdapter;
import com.fyrdev.monyia.adapters.driven.jpa.adapter.*;
import com.fyrdev.monyia.adapters.driven.jpa.mapper.*;
import com.fyrdev.monyia.adapters.driven.jpa.repository.*;
import com.fyrdev.monyia.configuration.security.jwt.JwtUtils;
import com.fyrdev.monyia.domain.api.*;
import com.fyrdev.monyia.domain.api.usecase.*;
import com.fyrdev.monyia.domain.spi.*;
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
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IPocketRepository pocketRepository;
    private final IPocketEntityMapper pocketEntityMapper;
    private final ITransactionRepository transactionRepository;
    private final ITransactionEntityMapper transactionEntityMapper;
    private final ILoanRepository loanRepository;
    private final ILoanEntityMapper loanEntityMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public OllamaClient ollamaClient() {
        return new OllamaClient();
    }

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
        return new UserUseCase(userPersistencePort(), encryptionPort(), pocketPersistencePort(), pocketServicePort());
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public AITextClassifierPort aiTextClassifierPort() {
        return new OllamaAdapter(ollamaClient());
    }

    @Bean
    public AiTextClassifierServicePort textClassifierServicePort() {
        return new AiClassifierUseCase(aiTextClassifierPort(), categoryServicePort());
    }

    @Bean
    public ICategoryServicePort categoryServicePort() {
        return new CategoryUseCase(categoryPersistencePort(), authenticationPort(), aiTextClassifierPort());
    }

    @Bean
    public IPocketPersistencePort pocketPersistencePort() {
        return new PocketAdapter(pocketRepository, pocketEntityMapper);
    }

    @Bean
    public IPocketServicePort pocketServicePort() {
        return new PocketUseCase(pocketPersistencePort(), authenticationPort(), aiTextClassifierPort());
    }

    @Bean
    public ITransactionPersistencePort transactionPersistencePort() {
        return new TransactionAdapter(transactionRepository, transactionEntityMapper);
    }

    @Bean
    public ITransactionServicePort transactionServicePort() {
        return new TransactionUseCase(transactionPersistencePort(), pocketPersistencePort(), authenticationPort(), categoryPersistencePort());
    }

    @Bean
    public ILoanPersistencePort loanPersistencePort() {
        return new LoanAdapter(loanRepository, loanEntityMapper);
    }

    @Bean
    public PocketUseCase pocketUseCase() {
        return new PocketUseCase(pocketPersistencePort(), authenticationPort(), aiTextClassifierPort());
    }

    @Bean
    public TransactionUseCase transactionUseCase() {
        return new TransactionUseCase(transactionPersistencePort(), pocketPersistencePort(), authenticationPort(), categoryPersistencePort());
    }

    @Bean
    public CategoryUseCase categoryUseCase() {
        return new CategoryUseCase(categoryPersistencePort(), authenticationPort(), aiTextClassifierPort());
    }

    @Bean
    public ILoanServicePort loanServicePort() {
        return new LoanUseCase(authenticationPort(),
                loanPersistencePort(),
                pocketUseCase(),
                transactionUseCase(),
                categoryUseCase());
    }
}
