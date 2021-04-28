package com.elow.codechallenge.NotificationService;

import com.elow.codechallenge.input.CurrencyConversionRate;

/**
 * A message for spot changes
 */
public class SpotChangeMessage extends Message {

    public SpotChangeMessage(CurrencyConversionRate rate) {
        super(rate);
    }

    @Override
    public String toString() {
        return String.format("{ \"timestamp\": %s, \"currencyPair\": \"%s\", \"alert\": \"spotChange\" }", rate.getTimestamp().toEpochMilli()/1000, rate.getCurrencyPair());
    }
}
