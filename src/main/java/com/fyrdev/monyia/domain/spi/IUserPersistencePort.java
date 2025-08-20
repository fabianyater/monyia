package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.User;

import java.util.List;

public interface IUserPersistencePort {
    User saveNewUser(User user);
    boolean isEmailExists(String email);
    User getUserById(Long id);
    List<User> getUsers();
}
