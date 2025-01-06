package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.PocketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPocketRepository extends JpaRepository<PocketEntity, Long> {
}
