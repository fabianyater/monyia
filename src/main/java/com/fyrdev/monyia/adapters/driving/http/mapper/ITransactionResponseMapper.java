package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.TransactionRequest;
import com.fyrdev.monyia.adapters.driving.http.dto.response.TransactionResponse;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.TransactionResponseSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITransactionResponseMapper {

    TransactionResponse toTransactionResponse(Transaction transaction);

    @Mapping(target = "value", source = "amount")
    @Mapping(target = "category", source = "categoryName")
    TransactionResponse toTransactionResponse(TransactionResponseSummary transactionResponseSummary);
    List<TransactionResponse> toTransactionResponseList(List<TransactionResponseSummary> transactions);
}
