package com.elow.codechallenge.NotificationService;

import com.elow.codechallenge.input.CurrencyConversionRate;

/**
 * A message for Streaking Changes
 */
public class StreakingChangeMessage extends Message {

    final StreakingChangeMessageAlertType alertType;
    final int duration;
    public StreakingChangeMessage(CurrencyConversionRate rate, StreakingChangeMessageAlertType alertType, int duration) {
        super(rate);
        this.alertType = alertType;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("{ \"timestamp\": %s, \"currencyPair\": \"%s\", \"alert\": \"%s\", seconds: %s }", rate.getTimestamp().toEpochMilli()/1000, rate.getCurrencyPair(), alertType, duration);
    }
}
