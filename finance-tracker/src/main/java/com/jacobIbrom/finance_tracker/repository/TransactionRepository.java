package com.jacobIbrom.finance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jacobIbrom.finance_tracker.model.CategoryType;
import com.jacobIbrom.finance_tracker.model.Transaction;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    List<Transaction> findByUserIdAndCategoryId(Long userId,Long categoryId);


@Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
       "WHERE t.user.id = :userId " +
       "AND t.category.type = :type " +
       "AND MONTH(t.date) = :month " +
       "AND YEAR(t.date) = :year")
BigDecimal sumByUserIdAndTypeAndMonthAndYear(
    @Param("userId") Long userId,
    @Param("type") CategoryType type,
    @Param("month") int month,
    @Param("year") int year
);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.id = :userId " +
           "AND t.category.id = :categoryId " +
           "AND MONTH(t.date) = :month " +
           "AND YEAR(t.date) = :year")
    BigDecimal sumByUserIdAndCategoryIdAndMonthAndYear(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("month") int month,
        @Param("year") int year);
}
