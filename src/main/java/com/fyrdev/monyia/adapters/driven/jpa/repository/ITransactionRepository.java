package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
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
            WHERE t.transactionType IN :types
            AND c.userEntity.id = :userId
            AND t.pocketEntity.id = :pocketId
            AND t.date BETWEEN :startDate AND :endDate
            GROUP BY c.id, c.name, c.defaultEmoji
            """)
    List<Object[]> getTotalAmountByCategoryGrouped(
            @Param("types") List<TransactionType> types,
            @Param("userId") Long userId,
            @Param("pocketId") Long pocketId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
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
                pockets p ON t.pocket_entity_id = p.id
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
            inner join pockets p
            on
            	t.pocket_entity_id = p.id
            where
            	l.id = :loanId
            	and (
                 		(:loanType = 'BORROWER' and t.transaction_type = 'EXPENSE')
                 		or
                 		(:loanType = 'LENDER' and t.transaction_type = 'INCOME')
                 	)
            """, nativeQuery = true)
    List<Object[]> findLoanPaymentsByLoanIdAndType(
            @Param("loanId") Long loanId,
            @Param("loanType") String loanType
    );

    @Query(value = """
            select
            	t.id,
                ge.name,
            	c.default_emoji,
            	c."name" as "categoryName",
            	t.amount,
            	t."date",
            	t.transaction_type
            from
            	goals ge
            inner join transactions t
            on
            	t.goal_entity_id = ge.id
            inner join categories c
            on
            	t.category_entity_id = c.id
            where
            	ge.id = :goalId
            	and ge.user_entity_id = :userId
            """, nativeQuery = true)
    List<Object[]> findGoalTransactionByGoalId(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query(value = """
            select
                sum(t.amount)
            from
                transactions t
            inner join pockets p on t.pocket_entity_id = p.id
            where
                p.user_entity_id = :userId
                and p.id = :pocketId
                and t.transaction_type = :type
                and t.date between :startDate and :endDate
            """, nativeQuery = true)
    Double sumByUserAndPocketAndDateRangeAndType(
            @Param("userId") Long userId,
            @Param("pocketId") Long pocketId,
            @Param("type") String type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("select t from TransactionEntity t where t.pocketEntity.id = ?1 and t.pocketEntity.userEntity.id = ?2")
    List<TransactionEntity> findLatestTransactions(Long id, Long id1);

    @Query("""
                SELECT t FROM TransactionEntity t
                WHERE t.pocketEntity.id = :pocketId
                  AND t.pocketEntity.userEntity.id = :userId
                  AND t.date BETWEEN :startDate AND :endDate
            """)
    List<TransactionEntity> findByPocketAndUserAndMonth(
            @Param("pocketId") Long pocketId,
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}