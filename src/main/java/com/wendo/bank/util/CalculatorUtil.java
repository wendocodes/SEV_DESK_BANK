package com.wendo.bank.util;

import com.wendo.bank.dto.AmountDto;
import com.wendo.bank.enitity.Amount;

public class CalculatorUtil {

    public static Amount addAmount(Amount a, Amount b) {
        return new Amount(a.getValue()+b.getValue(), a.getCurrency());
    }

    public static Amount subAmount(Amount a, Amount b) {
        return new Amount(a.getValue() - b.getValue(), a.getCurrency());
    }

    public static Amount addAmount(Amount a, Double b) {
        return new Amount(a.getValue()+b, a.getCurrency());
    }

    public static Amount subAmount(Amount a, Double b) {
        return new Amount(a.getValue() - b, a.getCurrency());
    }
    public static Amount subAmount(Double a, Double b, String currency) {
        return new Amount(a - b, currency);
    }

    public static Amount addAmount(Double a, Double b, String currency) {
        return new Amount(a + b, currency);
    }

    public static Amount addInterest(Double a, Double rate, String currency) {
        return addAmount(a, a * (rate/100), currency);
    }

    public static Amount addAmount(Amount balance, AmountDto amount) {
        return addAmount(balance, amount.getValue());
    }

    public static Amount subAmount(Amount balance, AmountDto amount) {
        return subAmount(balance, amount.getValue());
    }
}
