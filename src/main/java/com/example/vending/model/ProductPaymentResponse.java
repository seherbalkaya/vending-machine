package com.example.vending.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPaymentResponse {
    private String paymentType;
    private Double refundAmount;
    private Double totalPrice;
    private String productName;
    private Integer productCount;
}
