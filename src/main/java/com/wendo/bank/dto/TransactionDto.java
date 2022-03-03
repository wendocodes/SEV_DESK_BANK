package com.wendo.bank.dto;

import com.wendo.bank.enums.TransactionStatus;
import com.wendo.bank.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto implements Serializable {
    private ZonedDateTime createdTimestamp;
    private ZonedDateTime updatedTimestamp;
    private TransactionType type;
    private String category;
    private String merchant;
    private String location;
    private String ref;
    private TransactionStatus status;
    private String toAccountNumber;
    private String fromAccountNumber;
}
