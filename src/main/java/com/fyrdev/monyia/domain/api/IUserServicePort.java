package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.User;

public interface IUserServicePort {
    void saveNewUser(User user);
}
