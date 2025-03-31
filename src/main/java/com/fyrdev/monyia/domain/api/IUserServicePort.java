package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.User;

public interface IUserServicePort {
    User saveNewUser(User user);
    User getUserById(Long id);
}
