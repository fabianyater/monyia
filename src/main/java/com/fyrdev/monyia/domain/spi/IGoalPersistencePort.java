package com.fyrdev.monyia.domain.spi;

import com.fyrdev.monyia.domain.model.Goal;
import com.fyrdev.monyia.domain.model.Transaction;

import java.util.List;

public interface IGoalPersistencePort {
    Goal createNewGoal(Goal goal);
    boolean goalExists(Long goalId, Long userId);
    Goal getGoalById(Long goalId, Long userId);
    List<Goal> getAllGoalsByUserId(Long userId);
}
