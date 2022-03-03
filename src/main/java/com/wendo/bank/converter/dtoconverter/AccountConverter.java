package com.wendo.bank.converter.dtoconverter;

import com.wendo.bank.dto.AccountDto;
import com.wendo.bank.dto.AmountDto;
import com.wendo.bank.dto.CardDto;
import com.wendo.bank.dto.TransactionDto;
import com.wendo.bank.enitity.Account;
import com.wendo.bank.enitity.Amount;
import com.wendo.bank.enitity.Card;
import com.wendo.bank.enitity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter implements Converter<AccountDto, Account> {

    private final Converter<AmountDto, Amount> amountDtoConverter;
    private final Converter<CardDto, Card> cardConverter;
    private final Converter<TransactionDto, Transaction> transactionConverter;
    private final StatusConverter statusConverter;

    public AccountConverter(AmountDtoConverter amountDtoConverter, CardConverter cardConverter, TransactionConverter transactionConverter, StatusConverter statusConverter) {
        this.amountDtoConverter = amountDtoConverter;
        this.cardConverter = cardConverter;
        this.transactionConverter = transactionConverter;
        this.statusConverter = statusConverter;
    }

    @Override
    public Account convertToEntity(AccountDto accountDto) {
        return Account.builder()
                .accountNumber(accountDto.getAccountNumber())
                .name(accountDto.getName())
                .balance(amountDtoConverter.convertToEntity(accountDto.getBalance()))
                .overdraftLimit(amountDtoConverter.convertToEntity(accountDto.getBalance()))
                .build();
    }

    @Override
    public AccountDto convertToDto(Account account) {
        return AccountDto.builder()
                .accountNumber(account.getAccountNumber())
                .balance(amountDtoConverter.convertToDto(account.getBalance()))
                .overdraftLimit(amountDtoConverter.convertToDto(account.getOverdraftLimit()))
                .createdTimestamp(account.getCreatedTimestamp())
                .updatedTimestamp(account.getUpdatedTimestamp())
                .name(account.getName())
                .type(account.getType())
                .cards(cardConverter.convertToDtoList(account.getCards()))
                .transactions(transactionConverter.convertToDtoList(account.getTransactions()))
                .status(statusConverter.convertToDto(account.getStatus()))
                .build();
    }
}
