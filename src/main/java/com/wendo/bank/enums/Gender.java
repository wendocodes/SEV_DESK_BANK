package com.wendo.bank.enums;

public enum Gender {
    MASCULINE('M'), FEMININE('F'), NEUTER('N');

    private char code;

    Gender(char code) {
        this.code = code;
    }

    public char getCode(){
        return this.code;
    }

}
