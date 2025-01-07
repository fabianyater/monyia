package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import com.fyrdev.monyia.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITransactionEntityMapper {
    @Mapping(target = "categoryEntity.id", source = "categoryId")
    @Mapping(target = "pocketEntity.id", source = "pocketId")
    TransactionEntity toEntity(Transaction transaction);
}
