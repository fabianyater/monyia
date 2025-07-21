package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.request.LoanRequest;
import com.fyrdev.monyia.domain.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ILoanRequestMapper {
    Loan toLoan(LoanRequest loanRequest);
    LoanRequest toLoanRequest(Loan loan);

}
