package com.wendo.bank.service;

import com.wendo.bank.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto viewTransaction(String ref);
    List<TransactionDto> viewTransactions(String accountNumber);
}
