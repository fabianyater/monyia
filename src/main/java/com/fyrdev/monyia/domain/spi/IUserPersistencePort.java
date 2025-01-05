package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.User;

public interface IUserPersistencePort {
    void saveNewUser(User user);
    boolean isEmailExists(String email);
}
