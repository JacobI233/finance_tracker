package com.jacobIbrom.finance_tracker.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jacobIbrom.finance_tracker.dto.BudgetSummary;
import com.jacobIbrom.finance_tracker.dto.MonthlySummary;
import com.jacobIbrom.finance_tracker.model.Budget;
import com.jacobIbrom.finance_tracker.model.CategoryType;
import com.jacobIbrom.finance_tracker.model.User;
import com.jacobIbrom.finance_tracker.repository.BudgetRepository;
import com.jacobIbrom.finance_tracker.repository.TransactionRepository;
import com.jacobIbrom.finance_tracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public MonthlySummary geMonthlySummary(String email, Integer month, Integer year) {
        User user = getUser(email);

    BigDecimal totalIncome = transactionRepository.sumByUserIdAndTypeAndMonthAndYear(
            user.getId(), CategoryType.INCOME, month, year);

    BigDecimal totalExpenses = transactionRepository.sumByUserIdAndTypeAndMonthAndYear(
            user.getId(), CategoryType.EXPENSE, month, year);
        
        BigDecimal netSavings = totalIncome.subtract(totalExpenses);

        List<Budget> budgets = budgetRepository.findByUserIdAndMonthAndYear(
                user.getId(), month, year);

        List<BudgetSummary> budgetSummaries = budgets.stream().map(budget -> {
            BigDecimal actualSpending = transactionRepository
                    .sumByUserIdAndCategoryIdAndMonthAndYear(
                            user.getId(), budget.getCategory().getId(), month, year);

            BigDecimal remaining = budget.getAmountLimit().subtract(actualSpending);

            BudgetSummary summary = new BudgetSummary();
            summary.setId(budget.getId());
            summary.setCategoryName(budget.getCategory().getName());
            summary.setBudgetLimit(budget.getAmountLimit());
            summary.setActualSpending(actualSpending);
            summary.setRemaining(remaining);
            summary.setOverBudget(remaining.compareTo(BigDecimal.ZERO) < 0);
            return summary;
        }).toList();

        MonthlySummary monthlySummary = new MonthlySummary();
        monthlySummary.setMonth(month);
        monthlySummary.setYear(year);
        monthlySummary.setTotalIncome(totalIncome);
        monthlySummary.setTotalExpenses(totalExpenses);
        monthlySummary.setNetSavings(netSavings);
        monthlySummary.setBudgetSummaries(budgetSummaries);
        return monthlySummary;
    }
}