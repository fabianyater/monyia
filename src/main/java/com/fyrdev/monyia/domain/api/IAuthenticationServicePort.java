package com.fyrdev.monyia.domain.api;

public interface IAuthenticationServicePort {
    String authenticate(String email, String password);
}
