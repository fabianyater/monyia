package com.fyrdev.monyia.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoanRequest(
        @NotNull(message = "Amount is required")
        Double amount,
        @NotNull(message = "La persona u entidad a la que le prestaste/tomaste dinero es requerida")
        String loanParty,
        String description,
        @NotNull(message = "El tipo de prestamo es requerido")
        String loanType,
        @NotNull(message = "El pocket id es requerido")
        Long pocketId) {
}
