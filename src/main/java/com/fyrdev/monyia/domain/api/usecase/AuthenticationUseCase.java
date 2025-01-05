package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.IAuthenticationServicePort;
import com.fyrdev.monyia.domain.exception.InvalidCredentialsException;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.util.DomainConstants;

public class AuthenticationUseCase implements IAuthenticationServicePort {
    private final IAuthenticationPort authenticationPort;

    public AuthenticationUseCase(IAuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @Override
    public String authenticate(String email, String password) {
        if (!authenticationPort.validateCredentials(email, password)) {
            throw new InvalidCredentialsException(DomainConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        return authenticationPort.generateToken(authenticationPort.authenticate(email, password));
    }
}
