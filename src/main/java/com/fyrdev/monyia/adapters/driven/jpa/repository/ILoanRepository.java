package com.fyrdev.monyia.adapters.driven.jpa.repository;

import com.fyrdev.monyia.adapters.driven.jpa.entity.LoanEntity;
import com.fyrdev.monyia.domain.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILoanRepository extends JpaRepository<LoanEntity, Long> {
}
