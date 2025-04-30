package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.domain.api.ILoanServicePort;
import com.fyrdev.monyia.domain.exception.InsufficientBalanceException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Loan;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.LoanType;
import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import com.fyrdev.monyia.domain.spi.IAuthenticationPort;
import com.fyrdev.monyia.domain.spi.ILoanPersistencePort;
import com.fyrdev.monyia.domain.util.DomainConstants;

import java.time.LocalDateTime;
import java.util.List;

public class LoanUseCase implements ILoanServicePort {
    private final IAuthenticationPort authenticationPort;
    private final ILoanPersistencePort loanPersistencePort;
    private final PocketUseCase pocketUseCase;
    private final TransactionUseCase transactionUseCase;
    private final CategoryUseCase categoryUseCase;

    public LoanUseCase(IAuthenticationPort authenticationPort,
                       ILoanPersistencePort loanPersistencePort,
                       PocketUseCase pocketUseCase,
                       TransactionUseCase transactionUseCase,
                       CategoryUseCase categoryUseCase) {
        this.authenticationPort = authenticationPort;
        this.loanPersistencePort = loanPersistencePort;
        this.pocketUseCase = pocketUseCase;
        this.transactionUseCase = transactionUseCase;
        this.categoryUseCase = categoryUseCase;
    }

    @Override
    public void saveLoan(Loan loan) {
        Double loanAmount = loan.getAmount();
        Pocket pocket = pocketUseCase.getPocketByIdAndUserId(loan.getPocketId());
        Long pocketId = pocket.getId();
        LoanType loanType = loan.getLoanType();
        String categoryName = loanType == LoanType.LENDER ? "He prestado" : "Me han prestado";
        Category existingCategory = categoryUseCase.getCategoryByName(categoryName);

        if (loanType.equals(LoanType.LENDER)) {
            if (!pocketUseCase.isPocketBalanceSufficient(pocketId, loanAmount)) {
                throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
            }
        }

        loan.setStartDate(LocalDateTime.now());
        loan.setBalance(loanAmount);

        Loan savedLoan = loanPersistencePort.saveLoan(loan);

        Transaction transaction = new Transaction();

        transaction.setAmount(loanAmount);
        transaction.setDescription(loan.getDescription());
        transaction.setTransactionType(loanType == LoanType.LENDER ? TransactionType.EXPENSE : TransactionType.INCOME);
        transaction.setDate(LocalDateTime.now());
        transaction.setPeriodicity(Periodicity.ONCE);
        transaction.setCategoryId(existingCategory.getId());
        transaction.setLoanId(savedLoan.getId());
        transaction.setPocketId(pocketId);

        transactionUseCase.saveNewTransaction(transaction);
    }

    @Override
    public List<Loan> getLoansByPocketId() {
        Long userId = authenticationPort.getAuthenticatedUserId();
        return loanPersistencePort.findAllLoans(userId);
    }

    @Override
    public Loan getLoanDetails(Long loanId) {
        return loanPersistencePort.findLoanDetails(loanId, authenticationPort.getAuthenticatedUserId());
    }
}
