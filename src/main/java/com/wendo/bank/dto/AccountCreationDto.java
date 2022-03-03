package com.wendo.bank.dto;

import com.wendo.bank.enums.AccountType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AccountCreationDto {
    @NotEmpty
    private String customerNumber;
    @NotEmpty
    private String name;
    @NotEmpty
    private AccountType type;
    @NotEmpty
    private AmountDto overdraftLimit;

    private Double annualInterestRate;
}
