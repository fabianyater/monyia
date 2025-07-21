package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.IGoalServicePort;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.InsufficientBalanceException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Goal;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.GoalTransactionType;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.AITextClassifierPort;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.IGoalPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GoalUseCase implements IGoalServicePort {
    private final IGoalPersistencePort goalPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final AITextClassifierPort aiTextClassifierPort;
    private final ITransactionServicePort transactionServicePort;
    private final ICategoryServicePort categoryServicePort;
    private final IPocketServicePort pocketServicePort;

    public GoalUseCase(IGoalPersistencePort goalPersistencePort,
                       IAuthenticationPort authenticationPort,
                       AITextClassifierPort aiTextClassifierPort,
                       ITransactionServicePort transactionServicePort,
                       ICategoryServicePort categoryServicePort, IPocketServicePort pocketServicePort) {
        this.goalPersistencePort = goalPersistencePort;
        this.authenticationPort = authenticationPort;
        this.aiTextClassifierPort = aiTextClassifierPort;
        this.transactionServicePort = transactionServicePort;
        this.categoryServicePort = categoryServicePort;
        this.pocketServicePort = pocketServicePort;
    }


    @Override
    public Goal createNewGoal(Goal goal) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        String emoji = aiTextClassifierPort.suggestEmojis(goal.getName()).get(0);

        goal.setUserId(userId);
        goal.setEmoji(emoji);
        goal.setBalance(goal.getAmount());

        return goalPersistencePort.createNewGoal(goal);
    }

    @Override
    public List<Goal> getAllGoalsByUserId() {
        Long userId = authenticationPort.getAuthenticatedUserId();

        return goalPersistencePort.getAllGoalsByUserId(userId);
    }

    @Override
    public Goal getGoalById(Long goalId) {
        Long userId = authenticationPort.getAuthenticatedUserId();

        if (!goalPersistencePort.goalExists(goalId, userId)) {
            throw new ResourceNotFoundException((DomainConstants.GOAL_NOT_FOUND));
        }

        return goalPersistencePort.getGoalById(goalId, userId);
    }

    @Override
    public void makeDepositOrWithdraw(Long goalId, Long pocketId, BigDecimal amount, GoalTransactionType type) {
        Goal goal = getGoalById(goalId);

        if (type.equals(GoalTransactionType.DEPOSIT)) {
            if (pocketServicePort.isPocketBalanceSufficient(pocketId, amount)) {
                throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
            }
        }

        if (amount.doubleValue() <= 0) {
            throw new InsufficientBalanceException(DomainConstants.INVALID_DEPOSIT_AMOUNT);
        }

        if (type.equals(GoalTransactionType.WITHDRAWAL)) {
            BigDecimal result = goal.getAmount().subtract(goal.getBalance());
            if (amount.doubleValue() < result.doubleValue()) {
                throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
            }
        }

        if (goal.getBalance().doubleValue() < amount.doubleValue()) {
            throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
        }

        Long userId = authenticationPort.getAuthenticatedUserId();
        String categoryName = setCategoryName(type);
        Category category = categoryServicePort.getCategoryByName(categoryName);

        goal.setBalance(updateBalance(type, amount, goal.getBalance()));
        goal.setUserId(userId);

        goalPersistencePort.createNewGoal(goal);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(setDescription(type, goal.getName()));
        transaction.setTransactionType(setTransactionType(type));
        transaction.setDate(LocalDateTime.now());
        transaction.setPeriodicity(Periodicity.ONCE);
        transaction.setCategoryId(category.getId());
        transaction.setGoalId(goal.getId());
        transaction.setPocketId(pocketId);

        transactionServicePort.saveNewTransaction(transaction);

    }

    private BigDecimal updateBalance(GoalTransactionType type, BigDecimal amount, BigDecimal currentBalance) {
        return type.equals(GoalTransactionType.DEPOSIT) ?
                currentBalance.subtract(amount) :
                currentBalance.add(amount);
    }

    private TransactionType setTransactionType(GoalTransactionType type) {
        return type.equals(GoalTransactionType.DEPOSIT) ?
                TransactionType.EXPENSE :
                TransactionType.INCOME;
    }

    private String setCategoryName(GoalTransactionType type) {
        return type.equals(GoalTransactionType.DEPOSIT) ?
                "Depósito" :
                "Retiro";
    }

    private String setDescription(GoalTransactionType type, String goalName) {
        return type.equals(GoalTransactionType.DEPOSIT) ?
                "Depósito a meta " + goalName :
                "Retiro de meta " + goalName;
    }

}
