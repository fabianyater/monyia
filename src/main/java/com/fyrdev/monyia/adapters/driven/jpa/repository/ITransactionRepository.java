package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.domain.model.LoanTransactionsResponse;
import com.fyrdev.monyia.domain.model.TransactionSummaryByCategoriesResponse;
import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM TransactionEntity t
                WHERE t.transactionType = 'INCOME'
                  AND t.pocketEntity.id = :pocketId
                  AND t.pocketEntity.userEntity.id = :userId
                  AND t.date >= :startOfMonth AND t.date < :startOfNextMonth
            """)
    BigDecimal getMonthlyIncome(@Param("pocketId") Long pocketId,
                                @Param("userId") Long userId,
                                @Param("startOfMonth") LocalDateTime startOfMonth,
                                @Param("startOfNextMonth") LocalDateTime startOfNextMonth);

    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM TransactionEntity t
                WHERE t.transactionType = 'EXPENSE'
                  AND t.pocketEntity.id = :pocketId
                  AND t.pocketEntity.userEntity.id = :userId
                  AND t.date >= :startOfMonth AND t.date < :startOfNextMonth
            """)
    BigDecimal getMonthlyExpense(@Param("pocketId") Long pocketId,
                                 @Param("userId") Long userId,
                                 @Param("startOfMonth") LocalDateTime startOfMonth,
                                 @Param("startOfNextMonth") LocalDateTime startOfNextMonth);

    @Query("""
            SELECT c.id, c.name, c.defaultEmoji, SUM(t.amount)
            FROM TransactionEntity t
            JOIN t.categoryEntity c
            WHERE t.transactionType = :type
            AND c.userEntity.id = :userId
            AND t.pocketEntity.id = :pocketId
            GROUP BY c.id, c.name, c.defaultEmoji
            """)
    List<Object[]> getTotalAmountByCategoryGrouped(
            @Param("type") TransactionType type,
            @Param("userId") Long userId,
            @Param("pocketId") Long pocketId
    );

    @Query(value = """
            SELECT
                t.id as id,
                t.description as description,
                t.amount as amount,
                t.date,
                c.name as categoryName,
                c.default_emoji as categoryEmoji
            FROM
                transactions t
            INNER JOIN
                pocket p ON t.pocket_entity_id = p.id
            INNER JOIN
                users u ON p.user_entity_id = u.id
            INNER JOIN
                categories c ON t.category_entity_id = c.id
            WHERE
                p.id = :pocketId
                AND t.transaction_type = :transactionType
                AND c.name = :categoryName
            """, nativeQuery = true)
    List<Object[]> findTransactionsByFilters(
            @Param("pocketId") Long pocketId,
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("transactionType") String transactionType,
            @Param("categoryName") String categoryName
    );

    @Query(value = """
            select
            	t.id,
            	c.default_emoji,
            	c.name as categoryName,
            	p.name as pocketName,
            	t.amount,
            	t."date"
            from
            	loans l
            inner join transactions t
            on
            	t.loan_entity_id = l.id
            inner join categories c
            on
            	t.category_entity_id = c.id
            inner join pocket p
            on
            	t.pocket_entity_id = p.id
            where
            	l.id = :loanId
            """, nativeQuery = true)
    List<Object[]> findLoanTransactions(@Param("loanId") Long loanId);
}