package com.jacobIbrom.finance_tracker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MonthlySummary {
    private Integer month;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netSavings;
    private List<BudgetSummary> budgetSummaries;
}