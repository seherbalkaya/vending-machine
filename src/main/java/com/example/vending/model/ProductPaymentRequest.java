package com.example.vending.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPaymentRequest {

    private String paymentType;
    private Double price;
    private String nfcCode;
    private String cardPassword;
    private String cardChipId;

}
