package com.fyrdev.monyia.domain.api.usecase;

import com.fyrdev.monyia.configuration.exceptionhandler.ResourceNotFoundException;
import com.fyrdev.monyia.domain.api.ILoanServicePort;
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
        String categoryName = loanType == LoanType.LENDER ? "Cobrar" : "Reembolsar";
        Category existingCategory = categoryUseCase.getCategoryByName(categoryName);

        if (loanType.equals(LoanType.LENDER)) {
            if (!pocketUseCase.isPocketBalanceSufficient(pocketId, loanAmount)) {
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
        Pocket pocket = pocketUseCase.getPocketByIdAndUserId(pocketId);
        Category category = categoryUseCase.getCategoryByName(categoryName);
        Long existingPocketId = pocket.getId();

        if (loan.getLoanType().equals(LoanType.BORROWER)) {
            if (!pocketUseCase.isPocketBalanceSufficient(existingPocketId, amount)) {
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

        transactionUseCase.saveNewTransaction(transaction);
    }
}
