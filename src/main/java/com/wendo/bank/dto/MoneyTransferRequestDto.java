package com.wendo.bank.dto;

import com.wendo.bank.enums.TransactionMode;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MoneyTransferRequestDto {
    @NotEmpty
    private String customerNumber;
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String toAccountNumber;
    @NotEmpty
    private String pin;
    @NotEmpty
    private AmountDto amount;
    @NotEmpty
    private boolean isExternalAccount=false;
    @NotEmpty
    private String category;
    @NotEmpty
    private String merchant;
    @NotEmpty
    private String location;
    @NotEmpty
    private TransactionMode transactionMode;

}
