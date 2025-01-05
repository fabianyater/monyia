package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.domain.spi.IEncryptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class EncryptionAdapter implements IEncryptionPort {
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
