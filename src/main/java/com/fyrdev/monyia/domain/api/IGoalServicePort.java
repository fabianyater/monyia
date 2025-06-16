package com.fyrdev.monyia.domain.api;

import com.fyrdev.monyia.domain.model.Goal;
import com.fyrdev.monyia.domain.model.enums.GoalTransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface IGoalServicePort {
    Goal createNewGoal(Goal goal);
    List<Goal> getAllGoalsByUserId();
    Goal getGoalById(Long goalId);
    void makeDepositOrWithdraw(Long goalId, BigDecimal amount, GoalTransactionType type);
}
