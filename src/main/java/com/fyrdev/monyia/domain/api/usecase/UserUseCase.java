package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.api.IUserServicePort;
import com.fyrdev.monyia.domain.exception.AlreadyExistsException;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.User;
import com.fyrdev.monyia.domain.spi.IEncryptionPort;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.spi.IUserPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserUseCase implements IUserServicePort {
    private final IUserPersistencePort userPersistencePort;
    private final IEncryptionPort encryptionPort;
    private final IPocketServicePort pocketServicePort;

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IEncryptionPort encryptionPort, IPocketPersistencePort pocketPersistencePort, IPocketServicePort pocketServicePort) {
        this.userPersistencePort = userPersistencePort;
        this.encryptionPort = encryptionPort;
        this.pocketServicePort = pocketServicePort;
    }

    @Override
    public User saveNewUser(User user) {
        if (userPersistencePort.isEmailExists(user.getEmail())) {
            throw new AlreadyExistsException(DomainConstants.EMAIL_ALREADY_EXISTS_MESSAGE);
        }

        if (user.getUuid() == null) {
            user.setUuid(UUID.randomUUID().toString());
        }

        user.setPassword(encryptionPort.encode(user.getPassword()));
        user.setColor(createRandomHexColor());

        var createdUser = userPersistencePort.saveNewUser(user);

        Pocket pocket = new Pocket();
        pocket.setBalance(0.0);
        pocket.setName("Mi bolsillo");
        pocket.setEmoji("\uD83D\uDC5B");

        pocketServicePort.saveNewPocket(pocket, createdUser.getId());

        return createdUser;
    }

    @Override
    public User getUserById(Long id) {
        User user = userPersistencePort.getUserById(id);

        if (user == null) {
            throw new IllegalArgumentException(DomainConstants.USER_NOT_FOUND_MESSAGE);
        }

        return user;
    }

    private String createRandomHexColor() {
        return "#" + String.format("%06x", Math.abs(new java.util.Random().nextInt()));
    }
}
