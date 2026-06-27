package com.jacobIbrom.finance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jacobIbrom.finance_tracker.model.Transaction;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    List<Transaction> findByUserIdAndCategoryId(Long userId,Long categoryId);



}
