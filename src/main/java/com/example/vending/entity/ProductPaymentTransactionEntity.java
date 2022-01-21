package com.example.vending.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "product_payment_transaction")
public class ProductPaymentTransactionEntity {
    @Id
    @Column(unique = true, nullable = false, length = 50)
    private String id;
    private String productId;
    private String productCode;
    private Integer productCount;
    private Double totalPrice;
    private String paymentType;
    private Double refundPrice;
    private LocalDateTime expireDate;
    private Integer status;
}
