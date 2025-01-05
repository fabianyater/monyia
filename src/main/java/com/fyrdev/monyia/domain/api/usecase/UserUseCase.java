package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.IUserServicePort;
import com.fyrdev.monyia.domain.exception.EmailAlreadyExistsException;
import com.fyrdev.monyia.domain.model.User;
import com.fyrdev.monyia.domain.spi.IEncryptionPort;
import com.fyrdev.monyia.domain.spi.IUserPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.util.UUID;

public class UserUseCase implements IUserServicePort {
    private final IUserPersistencePort userPersistencePort;
    private final IEncryptionPort encryptionPort;

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IEncryptionPort encryptionPort) {
        this.userPersistencePort = userPersistencePort;
        this.encryptionPort = encryptionPort;
    }

    @Override
    public void saveNewUser(User user) {
        if (userPersistencePort.isEmailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException(DomainConstants.EMAIL_ALREADY_EXISTS_MESSAGE);
        }

        if (user.getUuid() == null) {
            user.setUuid(UUID.randomUUID().toString());
        }

        user.setPassword(encryptionPort.encode(user.getPassword()));

        userPersistencePort.saveNewUser(user);
    }
}
