package com.fyrdev.monyia.adapters.driving.http.mapper;

import com.fyrdev.monyia.adapters.driving.http.dto.response.LoanDetailResponse;
import com.fyrdev.monyia.adapters.driving.http.dto.response.LoanResponse;
import com.fyrdev.monyia.domain.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ILoanResponseMapper {
    Loan toLoan(LoanResponse loanResponse);
    LoanResponse toLoanResponse(Loan loan);
    LoanDetailResponse toLoanDetailResponse(Loan loan);
    List<LoanResponse> toLoanResponseList(List<Loan> loans);
}
