package com.wendo.bank.dto;

import com.wendo.bank.enitity.Status;
import com.wendo.bank.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto implements Serializable {
    private ZonedDateTime createdTimestamp;
    private ZonedDateTime updatedTimestamp;
    private String cardNumber;
    private CardType type;
    private StatusDto status;
}
