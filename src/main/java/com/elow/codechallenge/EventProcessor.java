package com.elow.codechallenge;

import com.elow.codechallenge.Alerts.AlertService;
import com.elow.codechallenge.Alerts.AlertServiceFactory;
import com.elow.codechallenge.input.CurrencyConversionRate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A currency rate change event processor
 */
public class EventProcessor {

    // the history of rate changes for the currency pairs
    private Map<String, CurrencyConversionRateHistory> conversionRateHistoryMap = new HashMap<>();

    // a map of currency pairs and the related alerts
    private Map<String, List<AlertService>> alertServiceMap = new HashMap<>();

    /**
     * Process the rate change for events
     *
     * @param currencyConversionRate the new rate event
     */
    public void process(CurrencyConversionRate currencyConversionRate) {
        CurrencyConversionRateHistory history = insertRateEvent(conversionRateHistoryMap, currencyConversionRate);
        checkAlerts(alertServiceMap, history);
    }

    /**
     * Checks the currency pair history for alerts
     *
     * @param alertServiceMap a map of currency pairs and the related alerts
     * @param history the history of rate changes for the currency pairs
     */
    private void checkAlerts(Map<String, List<AlertService>> alertServiceMap, CurrencyConversionRateHistory history) {
        if (!alertServiceMap.containsKey(history.currencyPair)) {
            AlertServiceFactory alertServiceFactory = new AlertServiceFactory();
            alertServiceMap.put(history.currencyPair, alertServiceFactory.createAlertServices());
        }

        alertServiceMap.get(history.currencyPair).forEach(
            alertService -> alertService.reviewHistory(history)
        );
    }

    /**
     * Insert a rate event into the history and check for alerts
     *
     * @param conversionRateHistoryMap a map of currency pairs and conversions
     * @param currencyConversionRate the current rate
     * @return the updated conversionRateHistoryMap
     */
    private CurrencyConversionRateHistory  insertRateEvent(Map<String, CurrencyConversionRateHistory> conversionRateHistoryMap, CurrencyConversionRate currencyConversionRate) {
        CurrencyConversionRateHistory history;

        String currencyPair = currencyConversionRate.getCurrencyPair();
        if (!conversionRateHistoryMap.containsKey(currencyPair)){
            conversionRateHistoryMap.put(currencyPair, new CurrencyConversionRateHistory(currencyPair));
        }

        history = conversionRateHistoryMap.get(currencyPair);
        history.add(currencyConversionRate);

        return history;
    }
}
