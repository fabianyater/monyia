package com.fyrdev.monyia.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
public class PocketRequest {
    @NotNull(message = "Pocket name cannot be empty")
    @NotBlank(message = "Pocket balance cannot be empty")
    private String name;

    @NotNull(message = "Pocket balance cannot be empty")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Long balance;
}
