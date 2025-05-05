package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ICategoryServicePort;
import com.fyrdev.monyia.domain.api.ILoanServicePort;
import com.fyrdev.monyia.domain.api.IPocketServicePort;
import com.fyrdev.monyia.domain.api.ITransactionServicePort;
import com.fyrdev.monyia.domain.exception.InsufficientBalanceException;
import com.fyrdev.monyia.domain.model.Category;
import com.fyrdev.monyia.domain.model.Loan;
import com.fyrdev.monyia.domain.model.Pocket;
import com.fyrdev.monyia.domain.model.Transaction;
import com.fyrdev.monyia.domain.model.enums.LoanStatus;
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
    private final IPocketServicePort pocketServicePort;
    private final ICategoryServicePort categoryServicePort;
    private final ITransactionServicePort transactionServicePort;

    public LoanUseCase(IAuthenticationPort authenticationPort,
                       ILoanPersistencePort loanPersistencePort,
                       IPocketServicePort pocketServicePort,
                       ICategoryServicePort categoryServicePort, 
                       ITransactionServicePort transactionServicePort) {
        this.authenticationPort = authenticationPort;
        this.loanPersistencePort = loanPersistencePort;
        this.pocketServicePort = pocketServicePort;
        this.categoryServicePort = categoryServicePort;
        this.transactionServicePort = transactionServicePort;
    }


    @Override
    public void saveLoan(Loan loan) {
        Double loanAmount = loan.getAmount();
        Pocket pocket = pocketServicePort.getPocketByIdAndUserId(loan.getPocketId());
        Long pocketId = pocket.getId();
        LoanType loanType = loan.getLoanType();
        String categoryName = loanType == LoanType.LENDER ? "Cobrar" : "Reembolsar";
        Category existingCategory = categoryServicePort.getCategoryByName(categoryName);

        if (loanType.equals(LoanType.LENDER)) {
            if (pocketServicePort.isPocketBalanceSufficient(pocketId, loanAmount)) {
                throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
            }
        }

        loan.setStartDate(LocalDateTime.now());
        loan.setBalance(loanAmount);
        loan.setLoanStatus(LoanStatus.ACTIVE);

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

        transactionServicePort.saveNewTransaction(transaction);
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

    @Override
    public void makePayment(Long loanId, Long pocketId, Double amount) {
        Long userId = authenticationPort.getAuthenticatedUserId();
        Loan loan = loanPersistencePort.findLoanDetails(loanId, userId);

        if (loan == null) {
            throw new ResourceNotFoundException(DomainConstants.LOAN_NOT_FOUND_MESSAGE);
        }

        if (loan.getLoanType().equals(LoanType.BORROWER)) {
            if (loan.getBalance() < amount) {
                throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
            }
        }

        String categoryName = loan.getLoanType() == LoanType.LENDER ? "Cobrar" : "Reembolsar";
        Pocket pocket = pocketServicePort.getPocketByIdAndUserId(pocketId);
        Category category = categoryServicePort.getCategoryByName(categoryName);
        Long existingPocketId = pocket.getId();

        if (loan.getLoanType().equals(LoanType.BORROWER)) {
            if (pocketServicePort.isPocketBalanceSufficient(existingPocketId, amount)) {
                throw new InsufficientBalanceException(DomainConstants.INSUFFICIENT_BALANCE_MESSAGE);
            }
        }

        loan.setBalance(loan.getBalance() - amount);

        if (loan.getBalance() <= 0) {
            loan.setLoanStatus(LoanStatus.COMPLETED);
        }

        loanPersistencePort.saveLoan(loan);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(loan.getLoanType() == LoanType.LENDER ? "Cobro" : "Reembolso");
        transaction.setTransactionType(loan.getLoanType() == LoanType.LENDER ? TransactionType.INCOME : TransactionType.EXPENSE);
        transaction.setDate(LocalDateTime.now());
        transaction.setPeriodicity(Periodicity.ONCE);
        transaction.setCategoryId(category.getId());
        transaction.setLoanId(loan.getId());
        transaction.setPocketId(pocketId);

        transactionServicePort.saveNewTransaction(transaction);
    }
}
