package com.wendo.bank.service.account;

import com.wendo.bank.dto.*;

public interface AccountWriteOperations {
    AccountDto createAccount(AccountCreationDto accountCreationDto);
    AccountDto updateAccount(String customerNumber, AccountUpdationDto accountUpdationDto);
    AccountDto updateStatusOfAccount(AccountStateChangeRequestDto accountStateChangeRequestDto);
    CardDto updateStatusOfCard(CardStateChangeDto cardStateChangeDto);
}