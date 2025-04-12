package com.fyrdev.monyia.adapters.driven.jpa.repository;

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
            SELECT c.id, c.name, c.emojis, SUM(t.amount)
            FROM TransactionEntity t
            JOIN t.categoryEntity c
            WHERE t.transactionType = :type
            AND c.userEntity.id = :userId
            AND t.pocketEntity.id = :pocketId
            GROUP BY c.id, c.name, c.emojis
            """)
    List<Object[]> getTotalAmountByCategoryGrouped(
            @Param("type") TransactionType type,
            @Param("userId") Long userId,
            @Param("pocketId") Long pocketId
    );
}