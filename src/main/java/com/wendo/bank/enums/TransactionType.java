package com.wendo.bank.enums;

public enum TransactionType {
    DEBIT("DR"), CREDIT("CR");

    private String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
