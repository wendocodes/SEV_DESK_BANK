package com.wendo.bank.converter.dtoconverter;

import com.wendo.bank.dto.AmountDto;
import com.wendo.bank.enitity.Amount;
import org.springframework.stereotype.Component;

@Component
public class AmountDtoConverter implements Converter<AmountDto, Amount> {
    public Amount convertToEntity(AmountDto amount) {
        return new Amount(amount.getValue(), amount.getCurrency());
    }

    public AmountDto convertToDto(Amount amount) {
        return new AmountDto(amount.getValue(), amount.getCurrency());
    }
}
