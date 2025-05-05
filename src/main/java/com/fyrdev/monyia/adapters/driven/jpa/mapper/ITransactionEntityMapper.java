package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.TransactionEntity;
import com.fyrdev.monyia.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITransactionEntityMapper {
    @Mapping(target = "categoryEntity.id", source = "categoryId")
    @Mapping(target = "pocketEntity.id", source = "pocketId")
    @Mapping(target = "loanEntity.id", source = "loanId")
    @Mapping(target = "goalEntity.id", source = "goalId")
    TransactionEntity toEntity(Transaction transaction);

    @Mapping(target = "categoryId", source = "categoryEntity.id")
    @Mapping(target = "pocketId", source = "pocketEntity.id")
    @Mapping(target = "loanId", source = "loanEntity.id")
    @Mapping(target = "goalId", source = "goalEntity.id")
    @Mapping(target = "toPocketId", source = "destinationPocketEntity.id")
    Transaction toTransaction(TransactionEntity transactionEntity);
    List<Transaction> toTransactionList(List<TransactionEntity> transactionEntities);
}
