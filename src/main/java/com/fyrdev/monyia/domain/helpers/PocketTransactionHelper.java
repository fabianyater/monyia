package com.fyrdev.monyia.domain.helpers;

import com.fyrdev.monyia.domain.exception.InsufficientBalanceException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.User;
import com.fyrdev.monyia.domain.model.dto.IncomeAndExpenseSummary;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;
import com.fyrdev.monyia.domain.spi.IUserPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PocketTransactionHelper {
    private final IPocketPersistencePort pocketPersistencePort;
    private final ITransactionPersistencePort transactionPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    public PocketTransactionHelper(IPocketPersistencePort pocketPersistencePort,
                                   ITransactionPersistencePort transactionPersistencePort,
                                   IUserPersistencePort userPersistencePort) {
        this.pocketPersistencePort = pocketPersistencePort;
        this.transactionPersistencePort = transactionPersistencePort;
        this.userPersistencePort = userPersistencePort;
    }

    public void transferBetweenPockets(Long fromPocketId, Long toPocketId, BigDecimal amount, Long userId, Category category) {
        Pocket fromPocket = pocketPersistencePort.getPocketByIdAndUserId(fromPocketId, userId);
        Pocket toPocket = pocketPersistencePort.getPocketByIdAndUserId(toPocketId, userId);

        if (fromPocket.getBalance().doubleValue() < amount.doubleValue()) {
            throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
        }

        fromPocket.setBalance(fromPocket.getBalance().subtract(amount));
        toPocket.setBalance(toPocket.getBalance().add(amount));

        pocketPersistencePort.updateBalanceById(fromPocket.getBalance(), fromPocket.getId());
        pocketPersistencePort.updateBalanceById(toPocket.getBalance(), toPocket.getId());

        Transaction fromTransaction = createNewTransaction(amount, fromPocket, toPocket, category, true);
        Transaction toTransaction = createNewTransaction(amount, fromPocket, toPocket, category, false);

        transactionPersistencePort.saveNewTransaction(fromTransaction);
        transactionPersistencePort.saveNewTransaction(toTransaction);
    }


    public Pocket getPocketByIdAndUserId(Long pocketId, Long userId) {
        return pocketPersistencePort.getPocketByIdAndUserId(pocketId, userId);
    }

    public List<IncomeAndExpenseSummary> getMonthlySummary(Long pocketId, Long userId, LocalDateTime startOfPreviousMonth){
        return transactionPersistencePort.getMonthlyIncomeAndExpenseSummary(pocketId, userId, startOfPreviousMonth);
    }

    public void makeTransactionWithPreviousBalance(Long categoryId) {
        List<User> users = userPersistencePort.getUsers();

        if (!users.isEmpty()) {
            users.forEach(user -> {
                List<Pocket> pockets = pocketPersistencePort.getPocketsByUserId(user.getId());
                LocalDate firstDayOfMonth = LocalDate.now();
                LocalDate startOfPreviousMonth = firstDayOfMonth.minusMonths(1).withDayOfMonth(1);

                pockets.forEach(pocket -> {
                    var summary = getMonthlySummary(pocket.getId(), user.getId(), startOfPreviousMonth.atStartOfDay());

                    BigDecimal previousIncome = getAmountFromType(summary, TransactionType.INCOME);
                    BigDecimal previousExpense = getAmountFromType(summary, TransactionType.EXPENSE);
                    BigDecimal previousMonthlyNetChange = previousIncome.subtract(previousExpense);

                    if (!previousMonthlyNetChange.equals(BigDecimal.ZERO)) {
                        Transaction transaction = new Transaction();
                        transaction.setPocketId(pocket.getId());
                        transaction.setAmount(previousMonthlyNetChange);
                        transaction.setTransactionType(TransactionType.INCOME);
                        transaction.setDate(firstDayOfMonth.atStartOfDay());
                        transaction.setDescription("Saldo anterior");
                        transaction.setUuid(UUID.randomUUID());
                        transaction.setPeriodicity(Periodicity.ONCE);
                        transaction.setCategoryId(categoryId);
                        transaction.setTransfer(Boolean.FALSE);

                        transactionPersistencePort.saveNewTransaction(transaction);
                    }
                });
            });
        }
    }

    private static Transaction createNewTransaction(BigDecimal amount, Pocket fromPocket, Pocket toPocket, Category category, boolean isFromPocket) {
        Transaction transaction = new Transaction();
        transaction.setPocketId(isFromPocket ? fromPocket.getId() : toPocket.getId());
        transaction.setToPocketId(isFromPocket ? toPocket.getId() : null);
        transaction.setAmount(amount);
        transaction.setTransactionType(isFromPocket ? TransactionType.EXPENSE : TransactionType.INCOME);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(setDescription(isFromPocket ? toPocket.getName() : fromPocket.getName(), isFromPocket));
        transaction.setUuid(UUID.randomUUID());
        transaction.setPeriodicity(Periodicity.ONCE);
        transaction.setCategoryId(category.getId());
        transaction.setTransfer(Boolean.TRUE);
        return transaction;
    }

    private static String setDescription(String name, boolean isFromPocket) {
        return isFromPocket ? "Transferencia a " + name : "Transferencia recibida de " + name;
    }


    public void updatePocketBalance(Pocket pocket, BigDecimal amount, TransactionType transactionType) {
        if (transactionType == TransactionType.EXPENSE) {
            pocketPersistencePort.updateBalanceById(pocket.getBalance().subtract(amount), pocket.getId());
        } else {
            pocketPersistencePort.updateBalanceById(pocket.getBalance().add(amount), pocket.getId());
        }
    }

    public static BigDecimal getAmountFromType(List<IncomeAndExpenseSummary> previousIncomeAndExpenseValues, TransactionType type) {
        Optional<IncomeAndExpenseSummary> result = previousIncomeAndExpenseValues.stream()
                .filter(incomeAndExpenseSummary -> incomeAndExpenseSummary
                        .type().equals(type.name()))
                .findFirst();

        return result.isPresent() ? result.get().amount() : BigDecimal.ZERO;
    }
}
