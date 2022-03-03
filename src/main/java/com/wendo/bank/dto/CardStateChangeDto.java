package com.wendo.bank.dto;

import com.wendo.bank.enums.AccountStatus;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CardStateChangeDto {
    @NotEmpty
    private String managerNumber;
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String cardNumber;
    @NotEmpty
    private StatusDto status;
}
