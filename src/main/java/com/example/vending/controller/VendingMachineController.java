package com.example.vending.controller;

import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.model.ProductPaymentResponse;
import com.example.vending.model.ProductResponse;
import com.example.vending.model.SelectedProductResponse;
import com.example.vending.service.VendingMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VendingMachineController {
    private final VendingMachineService vendingMachineService;

    @PostMapping("/product-type/{productCode}")
    public ResponseEntity<ProductResponse> selectProduct(@PathVariable("productCode") String productCode) {
        ProductResponse productResponse = vendingMachineService.selectProduct(productCode);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PatchMapping("/select-product/{productCode}")
    public ResponseEntity<SelectedProductResponse> selectedProductInfo(@PathVariable("productCode") String productCode,
                                                                       @RequestParam("transactionId") String transactionId,
                                                                       @RequestParam("count") Integer productCount) {
        SelectedProductResponse selectedProductResponse = vendingMachineService.selectedProductInfo(productCode, transactionId, productCount);
        return new ResponseEntity<>(selectedProductResponse, HttpStatus.OK);
    }

    @PatchMapping("/product-payment/{transactionId}")
    public ResponseEntity<ProductPaymentResponse> productPayment(@PathVariable("transactionId") String transactionId,
                                                                 @RequestBody ProductPaymentRequest productPaymentRequest) {
        ProductPaymentResponse productPaymentResponse = vendingMachineService.productPayment(transactionId, productPaymentRequest);
        return new ResponseEntity<>(productPaymentResponse, HttpStatus.OK);
    }
}
