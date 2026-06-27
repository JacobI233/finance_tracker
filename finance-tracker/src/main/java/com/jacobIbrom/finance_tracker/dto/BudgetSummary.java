package com.jacobIbrom.finance_tracker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetSummary {
    private Long id;
    private String categoryName;
    private BigDecimal budgetLimit;
    private BigDecimal actualSpending;
    private BigDecimal remaining;
    private boolean overBudget;
}