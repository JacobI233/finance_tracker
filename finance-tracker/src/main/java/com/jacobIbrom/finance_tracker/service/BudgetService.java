package com.jacobIbrom.finance_tracker.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.jacobIbrom.finance_tracker.dto.BudgetRequest;
import com.jacobIbrom.finance_tracker.dto.BudgetResponse;
import com.jacobIbrom.finance_tracker.model.Budget;
import com.jacobIbrom.finance_tracker.model.Category;
import com.jacobIbrom.finance_tracker.model.User;
import com.jacobIbrom.finance_tracker.repository.BudgetRepository;
import com.jacobIbrom.finance_tracker.repository.CategoryRepository;
import com.jacobIbrom.finance_tracker.repository.UserRepository;

import java.util.Objects;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //Create budget
    public BudgetResponse createBudget(BudgetRequest request, String email) {
        User user = getUser(email);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!Objects.equals(category.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
        user.getId(), category.getId(), request.getMonth(), request.getYear())
        .ifPresent(b -> { throw new RuntimeException("Budget already exists for this category and month"); });


        Budget budget = new Budget();
        budget.setAmountLimit(request.getAmountLimit());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setCategory(category);
        budget.setUser(user);

        return BudgetResponse.from(budgetRepository.save(budget));
    }
    //Get budget

    public List<BudgetResponse> getUserBudgets(String email,Integer month, Integer year) {
        User user = getUser(email);
        return budgetRepository.findByUserIdAndMonthAndYear(user.getId(),month,year)
                .stream()
                .map(BudgetResponse::from)
                .toList();
    }
    //Delete budget
    public void deleteBudget(Long id, String email) {
        User user = getUser(email);
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!Objects.equals(budget.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        budgetRepository.delete(budget);
    }



}
