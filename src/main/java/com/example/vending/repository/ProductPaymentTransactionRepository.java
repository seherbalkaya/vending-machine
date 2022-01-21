package com.example.vending.repository;

import com.example.vending.entity.ProductPaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPaymentTransactionRepository extends JpaRepository<ProductPaymentTransactionEntity, String> {
}
