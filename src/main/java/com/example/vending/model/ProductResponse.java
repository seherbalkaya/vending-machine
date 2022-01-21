package com.example.vending.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String productType;
    private Double productPrice;
    private String productName;
    private Integer productCount;
    private String transactionId;
    private LocalDateTime transactionExpireDate;
    private String productCode;

}
