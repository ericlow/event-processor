package com.elow.codechallenge.Alerts;

import com.elow.codechallenge.CurrencyConversionRateHistory;
import com.elow.codechallenge.NotificationService.Message;
import com.elow.codechallenge.NotificationService.SpotChangeMessage;

import java.util.Optional;

import static java.lang.Math.abs;

/**
 * Service that alerts for spot changes, default 10% of 5 minute moving average
 */
public class SpotChangeAlertService extends AlertService {

    // the threshold to notify on
    private double threshold;

    /**
     * Default constructor, notify on 10% change away from 5 min moving average
     */
    public SpotChangeAlertService() {
        this(.1);
    }

    /**
     * Parameterized constructor
     * @param threshold
     */
    public SpotChangeAlertService(double threshold) {
        this.threshold = threshold;
    }

    /**
     * send notification if there is a spot event
     * @param history
     */
    @Override
    public void reviewHistory(CurrencyConversionRateHistory history) {
        boolean isAlert = isSpotAlert(history);

        if (isAlert) {
            Message message = new SpotChangeMessage(history.getLastRate());
            notificationService.notify(message);
        }
    }

    /**
     * Determine if a spot alert has occurred against the 5 min moving average
     * @param history a history of currency conversion events for the rate pair
     * @return true if there is a spot alert, false if there is no spot alert
     */
    private boolean isSpotAlert(CurrencyConversionRateHistory history) {
        Optional<Double> movingAverage = history.getMovingAverage5().getAverage();
        if (movingAverage.isPresent()) {
            double last = history.getLastRate().getRate();

            double difference = abs((last-movingAverage.get())/movingAverage.get());

            String message = String.format("moving avg: %s\nlast rate: %s\n diff: %s \n %s \n --------", movingAverage.get(), last, difference, difference > .1);
            System.out.println(message);
            return difference > threshold;
        } else {
            return false;
        }
    }
}
