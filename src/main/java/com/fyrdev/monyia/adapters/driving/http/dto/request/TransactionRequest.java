package com.fyrdev.monyia.adapters.driving.http.dto.request;

import com.fyrdev.monyia.domain.model.enums.Periodicity;
import com.fyrdev.monyia.domain.model.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequest {
    @NotNull(message = "Transaction description cannot be empty")
    @NotBlank(message = "Transaction description cannot be empty")
    private String description;

    @NotNull(message = "Transaction amount cannot be empty")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal amount;

    @NotNull(message = "Transaction date cannot be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotNull(message = "Transaction periodicity cannot be empty")
    private Periodicity periodicity;

    @NotNull(message = "Transaction transaction type cannot be empty")
    private TransactionType transactionType;

    @NotNull(message = "Transaction category cannot be empty")
    private CategoryRequest category;

    @NotNull(message = "Transaction pocket id cannot be empty")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Long pocketId;
    private Long loanId;
}
