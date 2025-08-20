package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.domain.api.IBudgetServicePort;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.helpers.PocketTransactionHelper;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.dto.IncomeAndExpenseSummary;
import com.fyrdev.monyia.domain.model.dto.QueryFilters;
import com.fyrdev.monyia.domain.model.dto.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ITransactionPersistencePort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TransactionUseCase implements ITransactionServicePort {
    private final ITransactionPersistencePort transactionPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final ICategoryServicePort categoryServicePort;
    private final IBudgetServicePort budgetServicePort;
    private final PocketTransactionHelper pocketTransactionHelper;

    public TransactionUseCase(ITransactionPersistencePort transactionPersistencePort, IAuthenticationPort authenticationPort, ICategoryServicePort categoryServicePort, IBudgetServicePort budgetServicePort, PocketTransactionHelper pocketTransactionHelper) {
        this.transactionPersistencePort = transactionPersistencePort;
        this.authenticationPort = authenticationPort;
        this.categoryServicePort = categoryServicePort;
        this.budgetServicePort = budgetServicePort;
        this.pocketTransactionHelper = pocketTransactionHelper;
    }


    @Override
    public Transaction saveNewTransaction(Transaction transaction) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        var budget = budgetServicePort.updateBudgetBalance(transaction.getCategoryId(), transaction.getAmount());

        if (budget != null) {
            transaction.setBudgetId(budget.getId());
        }

        TransactionType type = transaction.getTransactionType();

        if (transaction.getUuid() == null) {
            transaction.setUuid(UUID.randomUUID());
        }

        if (transaction.getPocketId() != null) {
            Pocket pocket = pocketTransactionHelper.getPocketByIdAndUserId(transaction.getPocketId(), userId);
            transaction.setPocketId(pocket.getId());

            if (type != TransactionType.TRANSFER) {
                pocketTransactionHelper.updatePocketBalance(pocket, transaction.getAmount(), type);
            }
        }

        return transactionPersistencePort.saveNewTransaction(transaction);
    }

    @Override
    public List<IncomeAndExpenseSummary> getMonthlyIncomeAndExpenseSummary(Long pocketId, LocalDateTime startDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getMonthlyIncomeAndExpenseSummary(pocketId, userId, startDate);
    }

    @Override
    public List<TransactionSummaryByCategoriesResponse> getTransactionSummaryByCategories(Long pocketId, TransactionType transactionType, LocalDateTime startDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getTransactionSummaryByCategories(pocketId, userId, transactionType, startDate)
                .stream()
                .sorted(Comparator
                        .comparing(TransactionSummaryByCategoriesResponse::totalAmount)
                        .reversed())
                .toList()
                ;
    }

    @Override
    public List<TransactionResponse> listTransactionsByCategory(Long pocketId, TransactionType transactionType, String categoryName, LocalDateTime startDate, LocalDateTime endDate) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Category category = categoryServicePort.getCategoryByName(categoryName);

        var result = transactionPersistencePort
                .listTransactionsByCategory(pocketId, userId, category.getId(), transactionType, categoryName)
                .stream()
                .sorted(Comparator.comparing(TransactionResponse::date).reversed())
                .toList();

        if (startDate != null && endDate != null) {
            return result.stream()
                    .filter(txn ->
                            !txn.date().isBefore(startDate) &&
                            !txn.date().isAfter(endDate))
                    .toList();
        }

        return result;
    }

    @Override
    public List<TransactionResponse> findAllTransactionsByLoanId(Long loanId, String loanType) {

        return transactionPersistencePort.findAllTransactionsByLoanId(loanId, loanType)
                .stream()
                .sorted(Comparator.comparing(TransactionResponse::date).reversed())
                .toList();
    }

    @Override
    public List<TransactionResponse> findAllTransactionsByGoalId(Long goalId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.findAllTransactionsByGoalId(goalId, userId)
                .stream()
                .sorted(Comparator.comparing(TransactionResponse::date).reversed())
                .toList();
    }

    @Override
    public List<TransactionResponse> findAllTransactionsByBudgetId(Long budgetId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.findAllTransactionsByBudgetId(budgetId, userId)
                .stream()
                .sorted(Comparator.comparing(TransactionResponse::date).reversed())
                .toList();
    }

    @Override
    public List<Transaction> getLatestTransactionsByPocketId(Long pocketId) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort.getLatestTransactions(pocketId, userId)
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .limit(5)
                .toList();
    }

    @Override
    public List<TransactionResponse> getTransactionsByPocketId(Long pocketId, LocalDate startMonth) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return transactionPersistencePort
                .getTransactions(pocketId, userId, startMonth)
                .stream()
                .sorted(Comparator.comparing(TransactionResponse::date).reversed())
                .toList();
    }

    @Override
    public List<TransactionResponse> getAllTransactionsWithFilters(QueryFilters f, Integer page, Integer size, String order) {
        Long userId = authenticationPort.getAuthenticatedUserId();

        var transactions = transactionPersistencePort.getAllTransactionsByUserId(userId, page, size, order);

        Predicate<TransactionResponse> p = t -> true;

        if (nonBlank(f.category())) {
            String cat = normalize(f.category());
            p = p.and(t -> equalsIgnoreNormalized(cat, t.category()));
        }

        if (nonBlank(f.pocket())) {
            String cat = normalize(f.pocket());
            p = p.and(t -> equalsIgnoreNormalized(cat, t.pocketName()));
        }

        if (f.type() != null) {
            p = p.and(t -> Objects.equals(t.type(), f.type().name()));
        }

        if (nonBlank(f.pocket())) {
            String poc = normalize(f.pocket());
            p = p.and(t -> equalsIgnoreNormalized(poc, t.pocketName()));
        }

        if (nonBlank(f.text())) { // búsqueda "contiene" en varios campos
            String needle = normalize(f.text());
            p = p.and(t -> Stream.of(t.description(), t.category(), t.pocketName())
                    .filter(Objects::nonNull)
                    .map(this::normalize)
                    .anyMatch(s -> s.contains(needle)));
        }

        return transactions.stream()
                .filter(p)
                .sorted(Comparator.comparing(TransactionResponse::date).reversed())
                .toList();
    }

    private static boolean nonBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String normalize(String s) {
        String trimmed = s.trim().toLowerCase(Locale.ROOT);
        String noAccents = java.text.Normalizer
                .normalize(trimmed, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noAccents.replaceAll("\\s+", " ");
    }

    private boolean equalsIgnoreNormalized(String leftNormalized, String rightRaw) {
        if (rightRaw == null) return false;
        return leftNormalized.equals(normalize(rightRaw));
    }
}
