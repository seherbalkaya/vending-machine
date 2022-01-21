package com.example.vending.constant;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    STARTED(1),
    COMPLETED(2),
    FAILED(3);

    private final int id;

    TransactionStatus(int id) {
        this.id = id;
    }
}
