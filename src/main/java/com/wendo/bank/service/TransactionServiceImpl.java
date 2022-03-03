package com.wendo.bank.service;

import com.wendo.bank.converter.dtoconverter.TransactionConverter;
import com.wendo.bank.dto.TransactionDto;
import com.wendo.bank.enitity.Transaction;
import com.wendo.bank.exception.ResourceNotFoundException;
import com.wendo.bank.exception.ValidationException;
import com.wendo.bank.repo.TransactionRepository;
import com.wendo.bank.service.account.AccountReadOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  TransactionConverter transactionConverter) {
        this.transactionRepository = transactionRepository;
        this.transactionConverter = transactionConverter;
    }

    @Override
    public TransactionDto viewTransaction(String ref) {
        Transaction transaction = transactionRepository.findByRef(ref);
        if(transaction == null ) throw new ResourceNotFoundException("transaction with Reference number : %s not found", ref);
        return transactionConverter.convertToDto(transaction);
    }

    @Override
    public List<TransactionDto> viewTransactions(String accountNumber) {
        Set<Transaction> transactions = transactionRepository.findByAccountAccountNumber(accountNumber);
        if(transactions.isEmpty()) throw new ValidationException("no transactions found under account number : %s ", accountNumber);
        return transactionConverter.convertToDtoList(transactions);
    }
}
