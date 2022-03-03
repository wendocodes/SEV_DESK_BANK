package com.wendo.bank.service.account;

import com.wendo.bank.converter.dtoconverter.Converter;
import com.wendo.bank.dto.AccountDto;
import com.wendo.bank.enitity.Account;
import com.wendo.bank.enitity.User;
import com.wendo.bank.exception.ResourceNotFoundException;
import com.wendo.bank.repo.AccountRepository;
import com.wendo.bank.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountReadOperationsImpl implements AccountReadOperations{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Converter<AccountDto, Account> accountConverter;

    public AccountReadOperationsImpl(AccountRepository accountRepository, UserRepository userRepository, Converter<AccountDto, Account> accountConverter) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountConverter = accountConverter;
    }

    @Override
    public List<AccountDto> getByCustomer(String customerNumber) {
        User user = userRepository.findByCustomerNumber(customerNumber);
        if(user == null)
            throw new ResourceNotFoundException("Customer not found with number : %s", customerNumber);
        return user.getAccounts().stream().map(accountConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto getDetails(String customerNumber, String accountNumber) {
        User user = userRepository.findByCustomerNumber(customerNumber);
        if(user == null)
            throw new ResourceNotFoundException("Customer not found with number : %s", customerNumber);
        return user.getAccounts().stream()
                .filter(account -> accountNumber.equals(account.getAccountNumber()))
                .map(accountConverter::convertToDto).findAny().orElse(null);
    }

    @Override
    public boolean isAccountNumberValid(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}
