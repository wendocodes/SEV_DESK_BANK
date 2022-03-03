package com.wendo.bank.service;

import com.wendo.bank.dto.MoneyRequestDto;
import com.wendo.bank.dto.MoneyTransferAckDto;
import com.wendo.bank.dto.MoneyTransferRequestDto;
import com.wendo.bank.dto.TransactionDto;

public interface MoneyService {
    TransactionDto deposit(MoneyRequestDto moneyRequestDto);
    TransactionDto withdraw(MoneyRequestDto moneyRequestDto);
    TransactionDto transfer(MoneyTransferRequestDto moneyRequestDto);
    TransactionDto externalTransfer(MoneyTransferRequestDto moneyRequestDto);
    TransactionDto externalTransferAcknowledge(MoneyTransferAckDto moneyTransferAckDto);
}
