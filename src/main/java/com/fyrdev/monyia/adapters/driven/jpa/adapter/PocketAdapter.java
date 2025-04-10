package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.IPocketEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IPocketRepository;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PocketAdapter implements IPocketPersistencePort {
    private final IPocketRepository pocketRepository;
    private final IPocketEntityMapper pocketEntityMapper;

    @Override
    public void saveNewPocket(Pocket pocket) {
        pocketRepository.save(pocketEntityMapper.toEntity(pocket));
    }

    @Override
    public List<Pocket> getPocketsByUserId(Long userId) {
        return pocketRepository.findByUserEntity_Id(userId)
                .stream()
                .map(pocketEntityMapper::toPocket)
                .toList();
    }

    @Override
    public Pocket getPocketByIdAndUserId(Long pocketId, Long userId) {
        return pocketRepository.findByIdAndUserEntity_Id(pocketId, userId)
                .map(pocketEntityMapper::toPocket)
                .orElse(null);
    }

    @Override
    public Pocket getBalance(Long pocketId) {
        return pocketRepository
                .findById(pocketId)
                .map(pocketEntityMapper::toPocket)
                .orElse(null);
    }

    @Override
    public int updateBalanceById(Long balance, Long pocketId) {
        return pocketRepository.updateBalanceById(balance, pocketId);
    }
}
