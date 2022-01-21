package com.example.vending.service.impl;

import com.example.vending.model.ProductPaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CreditCardWithContactPayServiceTest {

    @InjectMocks
    private CreditCardWithContactPayService creditCardWithContactPayService;

    @Test
    void should_successfully_pay() {

        //given
        ProductPaymentRequest productPaymentRequest = new ProductPaymentRequest();
        productPaymentRequest.setPrice(5d);

        Double totalPrice = 5d;
        //when
        Double refundAmount = creditCardWithContactPayService.pay(productPaymentRequest, totalPrice);

        //then
        assertEquals(0d, refundAmount);
    }
}