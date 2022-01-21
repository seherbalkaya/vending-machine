package com.example.vending.service.impl;

import com.example.vending.model.ProductPaymentRequest;
import com.example.vending.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreditCardWithContactPayService implements PayService {

    Logger logger = LoggerFactory.getLogger(CreditCardWithContactPayService.class);

    @Override
    public Double pay(ProductPaymentRequest productPaymentRequest, Double totalPrice) {
        logger.info("payment transaction success");
        return 0d;
    }
}
