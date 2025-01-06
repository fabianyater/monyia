package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.User;

public interface IAuthenticationPort {
    User authenticate(String email, String password);
    boolean validateCredentials(String email, String password);
    String generateToken(User user);
    Long getAuthenticatedUserId();
}
