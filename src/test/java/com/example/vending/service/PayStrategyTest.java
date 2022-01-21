package com.example.vending.service;

import com.example.vending.constant.PaymentTypeDefinition;
import com.example.vending.exception.NotFoundPayStrategyException;
import com.example.vending.service.impl.BanknotePayService;
import com.example.vending.service.impl.CoinPayService;
import com.example.vending.service.impl.CreditCardWithContactPayService;
import com.example.vending.service.impl.CreditCartWithContactlessPayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PayStrategyTest {

    PayStrategy payStrategy;
    @Mock
    private BanknotePayService banknotePayService;
    @Mock
    private CoinPayService coinPayService;
    @Mock
    private CreditCardWithContactPayService creditCardWithContactPayService;
    @Mock
    private CreditCartWithContactlessPayService creditCartWithContactlessPayService;

    @BeforeEach
    void setUp() {
        payStrategy = new PayStrategy(banknotePayService, coinPayService, creditCardWithContactPayService, creditCartWithContactlessPayService);
    }

    @Test
    void should_successfully_when_coin_pay_strategy() {
        //given
        String paymentType = PaymentTypeDefinition.COIN.name();
        //when
        PayService payService = payStrategy.findStrategy(paymentType);
        //then
        assertEquals(coinPayService.getClass().getName(), payService.getClass().getName());
    }

    @Test
    void should_successfully_when_banknote_pay_strategy() {
        //given
        String paymentType = PaymentTypeDefinition.BANKNOTE.name();
        //when
        PayService payService = payStrategy.findStrategy(paymentType);
        //then
        assertEquals(banknotePayService.getClass().getName(), payService.getClass().getName());
    }

    @Test
    void should_successfully_when_credit_card_with_contact_pay_strategy() {
        //given
        String paymentType = PaymentTypeDefinition.CREDIT_CARD_WITH_CONTACT.name();
        //when
        PayService payService = payStrategy.findStrategy(paymentType);
        //then
        assertEquals(creditCardWithContactPayService.getClass().getName(), payService.getClass().getName());
    }

    @Test
    void should_successfully_when_credit_cart_with_contactless_pay_strategy() {
        //given
        String paymentType = PaymentTypeDefinition.CREDIT_CARD_WITH_CONTACTLESS.name();
        //when
        PayService payService = payStrategy.findStrategy(paymentType);
        //then
        assertEquals(creditCartWithContactlessPayService.getClass().getName(), payService.getClass().getName());
    }

    @Test
    void should_throw_notfound_pay_strategy_exception_when_find_pay_strategy() {
        //given
        String paymentType = "sodexo";
        //when
        RuntimeException e = assertThrows(RuntimeException.class, () -> payStrategy.findStrategy(paymentType));
        //then
        assertEquals(e.getClass(), NotFoundPayStrategyException.class);
    }
}