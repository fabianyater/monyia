package com.fyrdev.monyia.adapters.driving.http.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequest {
    @NotNull(message = "Transaction description cannot be empty")
    @NotBlank(message = "Transaction description cannot be empty")
    private String description;

    @NotNull(message = "Transaction amount cannot be empty")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Long amount;

    @NotNull(message = "Transaction date cannot be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotNull(message = "Transaction periodicity cannot be empty")
    @NotBlank(message = "Transaction periodicity cannot be empty")
    @Enumerated(EnumType.STRING)
    private String periodicity;

    @NotNull(message = "Transaction transaction type cannot be empty")
    @NotBlank(message = "Transaction transaction type cannot be empty")
    @Enumerated(EnumType.STRING)
    private String transactionType;

    @NotNull(message = "Transaction category id cannot be empty")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Long categoryId;

    @NotNull(message = "Transaction pocket id cannot be empty")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Long pocketId;
}
