package com.wendo.bank.dto;

import com.wendo.bank.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto implements Serializable {
    private AccountStatus status;
    private String description;
}
