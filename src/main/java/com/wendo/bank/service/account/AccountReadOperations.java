package com.wendo.bank.service.account;

import com.wendo.bank.dto.AccountDto;

import java.util.List;

public interface AccountReadOperations {
    List<AccountDto> getByCustomer(String customerNumber);
    AccountDto getDetails(String customerNumber, String accountNumber);
    boolean isAccountNumberValid(String accountNumber);
}
