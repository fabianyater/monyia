package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.entity.UserEntity;
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
    public User saveNewUser(User user) {
        UserEntity userEntity = userEntityMapper.toEntity(user);
        userRepository.save(userEntity);

        user = userEntityMapper.toUser(userEntity);

        return user;
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUser)
                .orElse(null);
    }
}
