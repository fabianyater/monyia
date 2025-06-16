package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.PocketEntity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IPocketRepository extends JpaRepository<PocketEntity, Long> {
    @Transactional
    @Modifying
    @Query("update PocketEntity p set p.balance = ?1 where p.id = ?2")
    int updateBalanceById(Double balance, Long id);

    Optional<PocketEntity> findByIdAndUserEntity_Id(Long id, Long id1);

    List<PocketEntity> findByUserEntity_Id(Long id);

    PocketEntity findByBalance(Long balance);

    @Query("select p.balance from PocketEntity p where p.id = ?1 and p.userEntity.id = ?2")
    Double findBalanceById(Long id, Long userId);

    @Query("select sum(p.balance) from PocketEntity p where p.userEntity.id = ?1 and p.excludeBalance = false")
    Double sumByUserEntity_Id(Long id);
}
