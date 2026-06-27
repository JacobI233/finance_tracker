package com.jacobIbrom.finance_tracker.dto;

import com.jacobIbrom.finance_tracker.model.Category;
import com.jacobIbrom.finance_tracker.model.CategoryType;

import lombok.Data;

@Data
public class CategoryResponse {
    
    private Long id;
    private String name;
    private CategoryType type;

    public static CategoryResponse from (Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());

        return response;
    }
}
