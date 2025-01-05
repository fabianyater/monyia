package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IUserRepository;
import com.fyrdev.monyia.domain.model.User;
import com.fyrdev.monyia.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAdapter implements IUserPersistencePort {
    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public void saveNewUser(User user) {
        userRepository.save(userEntityMapper.toEntity(user));
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
