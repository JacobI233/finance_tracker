package com.jacobIbrom.finance_tracker.controller;

import com.jacobIbrom.finance_tracker.dto.TransactionRequest;
import com.jacobIbrom.finance_tracker.dto.TransactionResponse;
import com.jacobIbrom.finance_tracker.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(transactionService.createTransaction(request, email));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(transactionService.getUserTransactions(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        transactionService.deleteTransaction(id, email);
        return ResponseEntity.ok("Transaction deleted");
    }
}