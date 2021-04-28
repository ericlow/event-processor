package com.elow.codechallenge;

import com.elow.codechallenge.input.CurrencyConversionRate;

/**
 * The history of currency rate conversions for a single pair of currencies
 */
public class CurrencyConversionRateHistory {

    // the currency rate that is tracked
    final String currencyPair;

    // the 5 minute moving average
    final MovingAverage movingAverage5;

    // the last rate that was added
    CurrencyConversionRate lastRate = null;

    /**
     * ctor for CurrencyConversionRateHistory
     * @param currencyPair the currency rate pair
     */
    public CurrencyConversionRateHistory(String currencyPair) {
        this(currencyPair, new MovingAverage(5 * 60));
    }

    /**
     * ctor for CurrencyConversionRateHistory
     * @param currencyPair the currency rate pair
     * @param movingAverage moving average implementation
     */
    public CurrencyConversionRateHistory(String currencyPair, MovingAverage movingAverage) {
        this.currencyPair = currencyPair;
        this.movingAverage5 = movingAverage;
    }


    /**
     * Adds a currency conversion rate to the history
     * @param currencyConversionRate a currency conversion rate event
     */
    public void add(CurrencyConversionRate currencyConversionRate) {
        movingAverage5.add(currencyConversionRate.getRate());

        lastRate = currencyConversionRate;
    }

    /**
     * The five minute moving average of events
     * @return the moving average
     */
    public MovingAverage getMovingAverage5() {
        return movingAverage5;
    }

    /**
     * the ID of the currency pair that is tracked
     * @return
     */
    public String getCurrencyPair() {
        return currencyPair;
    }

    /**
     *
     * @return the last rate added to the history
     */
    public CurrencyConversionRate getLastRate() {
        return lastRate;
    }
}
