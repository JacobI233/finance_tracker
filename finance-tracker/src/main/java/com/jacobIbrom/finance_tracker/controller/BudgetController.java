package com.jacobIbrom.finance_tracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jacobIbrom.finance_tracker.dto.BudgetRequest;
import com.jacobIbrom.finance_tracker.dto.BudgetResponse;
import com.jacobIbrom.finance_tracker.dto.TransactionRequest;
import com.jacobIbrom.finance_tracker.dto.TransactionResponse;
import com.jacobIbrom.finance_tracker.service.BudgetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @Valid @RequestBody BudgetRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(budgetService.createBudget(request, email));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(budgetService.getUserBudgets(email,month,year));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        budgetService.deleteBudget(id, email);
        return ResponseEntity.ok("Transaction deleted");
    }

}
