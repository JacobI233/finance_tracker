package com.jacobIbrom.finance_tracker.service;

import java.util.List;

import java.util.Objects;
import org.springframework.stereotype.Service;

import com.jacobIbrom.finance_tracker.dto.CategoryRequest;
import com.jacobIbrom.finance_tracker.model.Category;
import com.jacobIbrom.finance_tracker.model.User;
import com.jacobIbrom.finance_tracker.repository.CategoryRepository;
import com.jacobIbrom.finance_tracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    


    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getUser(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Category createCategory(CategoryRequest request, String email){

        User user = getUser(email);

        if (categoryRepository.existsByNameAndUserId(request.getName(), user.getId())) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setUser(user);
        return categoryRepository.save(category);

    }

    public List<Category> getUserCategories(String email){
        User user = getUser(email);
        return categoryRepository.findByUserId(user.getId());
    }

    public void deleteCategory(Long id, String email){
        User user = getUser(email);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        if (!Objects.equals(category.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        categoryRepository.delete(category);

    }



}
