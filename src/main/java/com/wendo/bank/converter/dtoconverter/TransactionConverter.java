package com.wendo.bank.converter.dtoconverter;

import com.wendo.bank.dto.TransactionDto;
import com.wendo.bank.enitity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter implements Converter<TransactionDto, Transaction> {


    @Override
    public Transaction convertToEntity(TransactionDto transactionDto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TransactionDto convertToDto(Transaction transaction) {
        return TransactionDto.builder()
                .createdTimestamp(transaction.getCreatedTimestamp())
                .updatedTimestamp(transaction.getUpdatedTimestamp())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .type(transaction.getType())
                .fromAccountNumber(transaction.getTransactionModeReferenceNumber())
                .toAccountNumber(transaction.getToAccountNumber())
                .location(transaction.getLocation())
                .merchant(transaction.getMerchant())
                .ref(transaction.getRef())
                .status(transaction.getStatus())
                .build();
    }
}
