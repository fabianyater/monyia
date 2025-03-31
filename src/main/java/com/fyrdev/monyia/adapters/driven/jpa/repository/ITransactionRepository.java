package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    SELECT COALESCE(SUM(CASE 
        WHEN t.transactionType = 'INCOME' THEN t.amount
        WHEN t.transactionType = 'EXPENSE' THEN -t.amount
        ELSE 0 END), 0)
    FROM TransactionEntity t
    WHERE t.pocketEntity.id = :pocketId
      AND t.pocketEntity.userEntity.id = :userId
      AND t.date >= :startOfMonth AND t.date < :startOfNextMonth
""")
    BigDecimal getMonthlyTotal(@Param("pocketId") Long pocketId,
                               @Param("userId") Long userId,
                               @Param("startOfMonth") LocalDateTime startOfMonth,
                               @Param("startOfNextMonth") LocalDateTime startOfNextMonth);


}