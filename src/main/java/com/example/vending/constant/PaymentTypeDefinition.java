package com.example.vending.constant;


public enum PaymentTypeDefinition {
    CREDIT_CARD_WITH_CONTACT("Kredi kartı ile temaslı ödeme"),
    CREDIT_CARD_WITH_CONTACTLESS("Kredi kartı ile temassız ödeme"),
    COIN("Bozuk para"),
    BANKNOTE("Kağıt para");

    private final String label;

    PaymentTypeDefinition(String label) {
        this.label = label;
    }
}
