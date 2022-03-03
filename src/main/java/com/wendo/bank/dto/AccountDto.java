package com.wendo.bank.dto;

import com.wendo.bank.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable {
    private ZonedDateTime createdTimestamp;
    private ZonedDateTime updatedTimestamp;
    private String accountNumber;
    private String name;
    private AmountDto balance;
    private AmountDto overdraftLimit;
    private AccountType type;
    private List<TransactionDto> transactions;
    private StatusDto status;
    private List<CardDto> cards;
}
