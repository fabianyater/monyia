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
    TransactionEntity toEntity(Transaction transaction);

    Transaction toTransaction(TransactionEntity transactionEntity);
    List<Transaction> toTransactionList(List<TransactionEntity> transactionEntities);
}
