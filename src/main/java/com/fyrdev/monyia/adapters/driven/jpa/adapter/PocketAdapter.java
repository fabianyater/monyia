package com.fyrdev.monyia.adapters.driven.jpa.adapter;

import com.fyrdev.monyia.adapters.driven.jpa.mapper.IPocketEntityMapper;
import com.fyrdev.monyia.adapters.driven.jpa.repository.IPocketRepository;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.spi.IPocketPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PocketAdapter implements IPocketPersistencePort {
    private final IPocketRepository pocketRepository;
    private final IPocketEntityMapper pocketEntityMapper;

    @Override
    public void saveNewPocket(Pocket pocket) {
        pocketRepository.save(pocketEntityMapper.toEntity(pocket));
    }
}
