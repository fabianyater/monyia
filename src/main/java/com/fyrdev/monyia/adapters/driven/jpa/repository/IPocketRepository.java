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
    int updateBalanceById(Long balance, Long id);

    Optional<PocketEntity> findByIdAndUserEntity_Id(Long id, Long id1);

    List<PocketEntity> findByUserEntity_Id(Long id);


}
