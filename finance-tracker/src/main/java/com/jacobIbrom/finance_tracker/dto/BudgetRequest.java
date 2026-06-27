package com.jacobIbrom.finance_tracker.dto;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BudgetRequest {
    
    
    @NotNull(message = "Amount Limit required")
    @Positive(message = "Amount Limit must be positive")
    private BigDecimal amountLimit;

    @NotNull(message = "Category required")
    private Long categoryId;

    @NotNull(message = "Month required")
    private Integer month;

    @NotNull(message = "Year required")
    private Integer year;

}




