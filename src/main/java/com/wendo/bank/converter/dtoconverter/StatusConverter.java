package com.wendo.bank.converter.dtoconverter;

import com.wendo.bank.dto.StatusDto;
import com.wendo.bank.enitity.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusConverter implements Converter<StatusDto, Status> {
    @Override
    public Status convertToEntity(StatusDto statusDto) {
        return new Status(statusDto.getStatus(), statusDto.getDescription());
    }

    @Override
    public StatusDto convertToDto(Status status) {
        if(status == null) return null;
        return new StatusDto(status.getStatus(), status.getDescription());
    }
}
