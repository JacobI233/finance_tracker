package com.jacobIbrom.finance_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jacobIbrom.finance_tracker.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{

    List<Category> findByUserId(Long userId);
    boolean existsByNameAndUserId(String name, long id);



}
