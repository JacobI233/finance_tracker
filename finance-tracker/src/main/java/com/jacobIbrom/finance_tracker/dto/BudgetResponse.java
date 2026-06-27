package com.jacobIbrom.finance_tracker.dto;

import java.math.BigDecimal;


import com.jacobIbrom.finance_tracker.model.Budget;

import lombok.Data;

@Data
public class BudgetResponse{

    private Long id;
    private BigDecimal amountLimit;
    private Integer month;
    private Integer year;
    private String categoryName;


    public static BudgetResponse from(Budget budget){
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setAmountLimit(budget.getAmountLimit());
        response.setMonth(budget.getMonth());
        response.setYear(budget.getYear());
        response.setCategoryName(budget.getCategory().getName()); 
        return response;

    }
    
    
}
