package com.example.vending.service;

import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.model.ProductPaymentResponse;
import com.example.vending.model.ProductResponse;
import com.example.vending.model.SelectedProductResponse;

public interface VendingMachineService {
    ProductResponse selectProduct(String productCode);

    SelectedProductResponse selectedProductInfo(String productCode, String transactionId, Integer productCount);

    ProductPaymentResponse productPayment(String transactionId, ProductPaymentRequest productPaymentRequest);

}
