package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ILoanRepository extends JpaRepository<LoanEntity, Long> {
    @Query(value = """
            select
            	l
            from
            	LoanEntity l
            inner join PocketEntity p
            on
            	l.pocketEntity.id = p.id
            inner join UserEntity u
            on
            	p.userEntity.id = :userId
            where
            	u.id = :userId
            """)
    List<LoanEntity> findAllLoansByUserId(
            @Param("userId")
            Long userId
    );

    @Query(value = """
            select
            	l
            from
            	LoanEntity l
            inner join PocketEntity p
            on
            	l.pocketEntity.id = p.id
            inner join UserEntity u
            on
            	p.userEntity.id = :userId
            where
            	u.id = :userId
            and
                l.id = :loanId
            """)
    Optional<LoanEntity> findLoanDetailsByLoanIdAndUserId(
            @Param("loanId")
            Long loanId,
            @Param("userId")
            Long userId
    );

    @Query(value = """
            select
            	sum(l.balance)
            from
            	loans l
            inner join pockets p
            on
            	l.pocket_entity_id = p.id
            inner join users u
            on
            	p.user_entity_id = u.id
            where
            	u.id = :id
            """, nativeQuery = true)
    Double sumTotalBalance(@Param("id") Long userId);

}
