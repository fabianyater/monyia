package com.fyrdev.monyia.domain.spi;

public interface IEncryptionPort {
    String encode(String password);
}