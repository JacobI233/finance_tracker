package com.jacobIbrom.finance_tracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacobIbrom.finance_tracker.dto.CategoryRequest;
import com.jacobIbrom.finance_tracker.dto.CategoryResponse;
import com.jacobIbrom.finance_tracker.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {



    private final CategoryService categoryService;


@PostMapping
public ResponseEntity<CategoryResponse> create(
        @Valid @RequestBody CategoryRequest request,
        @AuthenticationPrincipal String email) {
    return ResponseEntity.ok(CategoryResponse.from(categoryService.createCategory(request, email)));
}

@GetMapping
public ResponseEntity<List<CategoryResponse>> getAll(
        @AuthenticationPrincipal String email) {
    return ResponseEntity.ok(categoryService.getUserCategories(email)
            .stream()
            .map(CategoryResponse::from)
            .toList());
}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal String email){
            categoryService.deleteCategory(id, email);

            return ResponseEntity.ok("Category Deleted");
        }
    }