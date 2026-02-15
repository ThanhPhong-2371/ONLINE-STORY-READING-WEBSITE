package com.example.Nhom8.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Nhom8.models.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);

    // For Statistics
    // @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.status = 'SUCCESS'")
    // BigDecimal calculateTotalRevenue();
}
