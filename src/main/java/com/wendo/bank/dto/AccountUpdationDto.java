package com.wendo.bank.dto;

import com.wendo.bank.enums.AccountType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AccountUpdationDto {
    @NotEmpty
    private String customerNumber;
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String pin;
    private String name;
    private AccountType type;
    private AmountDto overdraftLimit;
    private String newPin;
}
