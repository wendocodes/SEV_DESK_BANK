package com.wendo.bank.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MoneyTransferAckDto {

    @NotEmpty
    private String ref;
    @NotEmpty
    private boolean success;
}
