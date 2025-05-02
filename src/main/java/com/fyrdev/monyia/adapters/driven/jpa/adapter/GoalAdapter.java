package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.IGoalEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IGoalRepository;
import com.fyrdev.monyia.domain.model.Goal;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.spi.IGoalPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GoalAdapter implements IGoalPersistencePort {
    private final IGoalRepository goalRepository;
    private final IGoalEntityMapper goalEntityMapper;

    @Override
    public Goal createNewGoal(Goal goal) {
        var goalEntity = goalEntityMapper.toGoalEntity(goal);
        var savedGoalEntity = goalRepository.save(goalEntity);

        return goalEntityMapper.toGoal(savedGoalEntity);
    }

    @Override
    public boolean goalExists(Long goalId, Long userId) {
        return goalRepository.existsByIdAndUserEntity_Id(goalId, userId);
    }

    @Override
    public Goal getGoalById(Long goalId, Long userId) {
        return goalRepository.findByIdAndUserEntity_Id(goalId, userId)
                .map(goalEntityMapper::toGoal)
                .orElse(null);
    }

    @Override
    public List<Goal> getAllGoalsByUserId(Long userId) {
        return goalRepository.findByUserEntity_Id(userId)
                .stream()
                .map(goalEntityMapper::toGoal)
                .toList();
    }
}
