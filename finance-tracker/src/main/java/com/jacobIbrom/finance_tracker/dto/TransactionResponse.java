package com.jacobIbrom.finance_tracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jacobIbrom.finance_tracker.model.CategoryType;
import com.jacobIbrom.finance_tracker.model.Transaction;

import lombok.Data;

@Data
public class TransactionResponse {
    

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private String categoryName;
    private CategoryType categoryType;

    public static TransactionResponse from(Transaction transaction){
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setDate(transaction.getDate());
        response.setCategoryName(transaction.getCategory().getName());
        response.setCategoryType(transaction.getCategory().getType());
        return response;

    }






}
