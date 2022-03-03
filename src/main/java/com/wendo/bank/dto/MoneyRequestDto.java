package com.wendo.bank.dto;

import com.wendo.bank.enums.TransactionMode;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MoneyRequestDto {
    @NotEmpty
    private String customerNumber;
    private String accountNumber;
    private String pin;
    @NotEmpty
    private AmountDto amount;
    @NotEmpty
    private String category;
    @NotEmpty
    private String merchant;
    @NotEmpty
    private String location;
    @NotEmpty
    private TransactionMode transactionMode;
    private String cardNumber;
    private String cardPin;

}
