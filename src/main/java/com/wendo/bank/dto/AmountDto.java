package com.wendo.bank.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AmountDto implements Serializable {
    private final Double value;
    private final String currency;
}
