package com.example.vending.service.impl;

import com.example.vending.entity.SafeEntity;
import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.repository.SafeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CoinPayServiceTest {

    @InjectMocks
    private CoinPayService coinPayService;

    @Mock
    private SafeRepository safeRepository;

    @Test
    void should_successfully_pay_request_when_zero_refund_amount() {
        //given
        ProductPaymentRequest productPaymentRequest = new ProductPaymentRequest();
        productPaymentRequest.setPrice(5d);

        Double totalPrice = 5d;

        SafeEntity safeEntity = new SafeEntity();
        safeEntity.setTotalMoney(100d);
        safeEntity.setId("1");

        Mockito.when(safeRepository.getById("1")).thenReturn(safeEntity);
        //when
        Double refundAmount = coinPayService.pay(productPaymentRequest, totalPrice);
        //then
        assertEquals(0d, refundAmount);
    }

    @Test
    void should_successfully_pay_request_when_two_refund_amount() {
        //given
        ProductPaymentRequest productPaymentRequest = new ProductPaymentRequest();
        productPaymentRequest.setPrice(5d);

        Double totalPrice = 3d;

        SafeEntity safeEntity = new SafeEntity();
        safeEntity.setTotalMoney(100d);
        safeEntity.setId("1");

        Mockito.when(safeRepository.getById("1")).thenReturn(safeEntity);
        //when
        Double refundAmount = coinPayService.pay(productPaymentRequest, totalPrice);
        //then
        assertEquals(2d, refundAmount);
    }
}