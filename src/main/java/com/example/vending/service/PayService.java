package com.example.vending.service;

import com.example.vending.model.ProductPaymentRequest;

public interface PayService {

    Double pay(ProductPaymentRequest productPaymentRequest, Double totalPrice);
}
