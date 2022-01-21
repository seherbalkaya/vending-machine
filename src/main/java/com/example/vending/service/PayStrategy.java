package com.example.vending.service;

import com.example.vending.constant.PaymentTypeDefinition;
import com.example.vending.exception.NotFoundPayStrategyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayStrategy {

    private final PayService banknotePayService;
    private final PayService coinPayService;
    private final PayService creditCardWithContactPayService;
    private final PayService creditCartWithContactlessPayService;

    public PayService findStrategy(String paymentType) {
        if (paymentType.equals(PaymentTypeDefinition.COIN.name())) {
            return coinPayService;
        } else if (paymentType.equals(PaymentTypeDefinition.BANKNOTE.name())) {
            return banknotePayService;
        } else if (paymentType.equals(PaymentTypeDefinition.CREDIT_CARD_WITH_CONTACT.name())) {
            return creditCardWithContactPayService;
        } else if (paymentType.equals(PaymentTypeDefinition.CREDIT_CARD_WITH_CONTACTLESS.name())) {
            return creditCartWithContactlessPayService;
        }
        throw new NotFoundPayStrategyException();
    }
}
