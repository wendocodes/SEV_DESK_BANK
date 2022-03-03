package com.wendo.bank.converter.dbconverter;

import com.wendo.bank.enums.TransactionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TransactionTypeDbConverter implements AttributeConverter<TransactionType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionType transactionType) {
        if (transactionType == null) {
            return null;
        }
        return transactionType.getCode();
    }

    @Override
    public TransactionType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(TransactionType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
