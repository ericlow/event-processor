package com.elow.codechallenge.Alerts;

import com.elow.codechallenge.CurrencyConversionRateHistory;
import com.elow.codechallenge.NotificationService.NotificationService;

/**
 * Service that sends alerts on inspection of currency rate changes
 */
public abstract class AlertService {

    protected final NotificationService notificationService = new NotificationService();

    public abstract void reviewHistory(CurrencyConversionRateHistory history);

}
