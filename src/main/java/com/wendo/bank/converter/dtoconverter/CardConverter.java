package com.wendo.bank.converter.dtoconverter;

import com.wendo.bank.dto.CardDto;
import com.wendo.bank.enitity.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardConverter implements Converter<CardDto, Card> {

    @Autowired
    private StatusConverter statusConverter;

    public Card convertToEntity(CardDto cardDto) {
        throw new UnsupportedOperationException();
    }

    public CardDto convertToDto(Card card) {
        return CardDto.builder()
                .createdTimestamp(card.getCreatedTimestamp())
                .updatedTimestamp(card.getUpdatedTimestamp())
                .cardNumber(card.getCardNumber())
                .type(card.getType())
                .status(statusConverter.convertToDto(card.getStatus()))
                .build();
    }
}
