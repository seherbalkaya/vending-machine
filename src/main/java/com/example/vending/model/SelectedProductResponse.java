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
public class SelectedProductResponse {

    private Double totalPrice;
    private Integer selectedCount;
    private String transactionId;
    private LocalDateTime transactionExpireDate;
    private String productCode;
}
