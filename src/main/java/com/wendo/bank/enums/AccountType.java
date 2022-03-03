package com.wendo.bank.enums;

public enum AccountType {
    CURRENT("C"), SAVINGS("S"), FIXED_DEPOSIT("FD");

    private String code;

    AccountType(String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}