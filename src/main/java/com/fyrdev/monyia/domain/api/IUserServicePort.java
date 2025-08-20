package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.adapters.driving.http.dto.response.UserResponse;
import com.fyrdev.monyia.domain.model.User;

import java.util.List;

public interface IUserServicePort {
    User saveNewUser(User user);
    User getUserById(Long id);
    List<User> getUsers();
}
