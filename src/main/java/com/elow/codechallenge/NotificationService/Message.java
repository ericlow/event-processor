package com.elow.codechallenge.NotificationService;

import com.elow.codechallenge.input.CurrencyConversionRate;

/**
 * Abstract base class of a message to be sent
 */
public abstract class Message {

    // the currency conversion rate event that triggered the message
    final protected CurrencyConversionRate rate;

    /**
     * The message constructor
     * @param rate the currency conversion rate event that triggered the message
     */
    public Message(CurrencyConversionRate rate) {
        this.rate = rate;
    }
}
