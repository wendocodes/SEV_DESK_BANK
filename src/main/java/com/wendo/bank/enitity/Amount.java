package com.wendo.bank.enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {

    private Double value;
    private String currency;


    @Override
    public String toString() {
        return currency +" " + value;
    }
}
