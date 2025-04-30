package com.fyrdev.monyia.adapters.driven.jpa.mapper;

import com.fyrdev.monyia.adapters.driven.jpa.entity.LoanEntity;
import com.fyrdev.monyia.domain.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ILoanEntityMapper {
    Loan toLoan(LoanEntity loanEntity);

    @Mapping(target = "pocketEntity.id", source = "pocketId")
    LoanEntity toLoanEntity(Loan loan);
}
