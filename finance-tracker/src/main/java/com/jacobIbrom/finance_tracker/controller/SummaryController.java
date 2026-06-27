package com.jacobIbrom.finance_tracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacobIbrom.finance_tracker.dto.MonthlySummary;
import com.jacobIbrom.finance_tracker.service.SummaryService;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/api/summary")
@RestController
@RequiredArgsConstructor
public class SummaryController {
    
    private final SummaryService summaryService;

    @GetMapping
    public ResponseEntity<MonthlySummary> getMonthlySummary(
        @RequestParam Integer month,
        @RequestParam Integer year,
        @AuthenticationPrincipal String email){
            return ResponseEntity.ok(summaryService.geMonthlySummary(email, month, year));
    }
}
