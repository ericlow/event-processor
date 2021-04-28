package com.elow.codechallenge.Alerts;

import com.elow.codechallenge.CurrencyConversionRateHistory;
import com.elow.codechallenge.NotificationService.NotificationService;
import com.elow.codechallenge.NotificationService.StreakingChangeMessage;
import com.elow.codechallenge.NotificationService.StreakingChangeMessageAlertType;
import com.elow.codechallenge.input.CurrencyConversionRate;

/**
 * A service to detect and notify of streaking type changes
 *
 * sends a notification after a duration has been monitored, and then only 1x per minute during the streak, afterwards
 */
public class StreakingChangeAlertService extends AlertService {

    // the current rate trend
    enum RisingStreamEnum {
        rising,
        falling,
        steady
    }

    // the last rate change value
    private Double lastRate = null;

    // the current direction of rate change
    private RisingStreamEnum streakType = RisingStreamEnum.steady;

    // the length of the streak
    private int streakCounter = 0;

    // the amount of seconds before a notification should be sent out
    private final int streakMaxSec;

    /**
     * Constructor for the StreakingChangeAlertService
     * @param streakMaxSec the number of seconds of streak before a notification is sent
     */
    public StreakingChangeAlertService(int streakMaxSec) {
        this.streakMaxSec = streakMaxSec;
    }

    /**
     * Review the history for streaking change events
     * @param history the history of rate change events
     */
    @Override
    public void reviewHistory(CurrencyConversionRateHistory history) {
        double newRate = history.getLastRate().getRate();

        if (lastRate != null) {
            if (streakCounter > 0) {
                if (streakType == RisingStreamEnum.rising && isRising(lastRate, newRate)) {
                    streakCounter++;
                } else if (streakType == RisingStreamEnum.falling && isFalling(lastRate, newRate)) {
                    streakCounter++;
                } else {
                    breakStreak();
                }
            } else {
                if (isRising(lastRate, newRate)) {
                    streakType = RisingStreamEnum.rising;
                    streakCounter++;
                } else if (isFalling(lastRate, newRate)) {
                    streakType = RisingStreamEnum.falling;
                    streakCounter++;
                } else {
                    streakType = RisingStreamEnum.steady;
                }
            }
        }
        lastRate = newRate;

        sendNotifcation(history, streakMaxSec, streakCounter);
    }

    /**
     * Send a notification if the event is alertable (a streak of minimum duration) and is throttled to minute
     * @param history the history of rate changes
     * @param streakMaxSec the time to monitor a streak before sending a message
     * @param streakCounter the duration of the current streak
     */
    private void sendNotifcation(CurrencyConversionRateHistory history, int streakMaxSec, int streakCounter) {
        if (isAlertableStreak(streakMaxSec, streakCounter) && isAlertableEvent(streakMaxSec, streakCounter)) {

            StreakingChangeMessage message = createStreakingChangeMessage(history.getLastRate(), streakType, streakCounter);
            NotificationService service = new NotificationService();
            service.notify(message);
        }
    }

    /**
     * Determines if the rates are increasing
     * @param lastRate the previous rate
     * @param newRate the new rate
     * @return true, if rising
     */
    private boolean isRising(double lastRate, double newRate) {
        return lastRate < newRate;
    }

    /**
     * Determines if rates are falling
     * @param lastRate the previous rate
     * @param newRate the current rate
     * @return true if falling
     */
    private boolean isFalling(double lastRate, double newRate) {
        return lastRate > newRate;
    }

    /**
     * reset tracking data if the current streak is broken
     */
    private void breakStreak() {
        streakCounter = 0;
        streakType = RisingStreamEnum.steady;
    }

    /**
     * Deetermine if the Streak is long enough to send an event
     * @param streakMaxSec the number of seconds to monitor a streak before it is elgible for notifications
     * @param streakCounter the current number of seconds that a streak has been going on
     * @return true, if the streak has been going long enough to send a notification
     */
    private boolean isAlertableStreak(int streakMaxSec, int streakCounter) {
        return streakCounter >= streakMaxSec;
    }

    /**
     * Determine if a notification should be sent; Only send notifications once per minute
     * @param streakMaxSec the number of seconds to monitor a streak before it is elgible for notifications
     * @param streakCounter the current number of seconds that a streak has been going on
     * @return true, if the streak should cause a notification to be sent
     */
    private boolean isAlertableEvent(int streakMaxSec, int streakCounter) {
        return (streakCounter - streakMaxSec) % 60 == 0;
    }

    /**
     * Create a message for the current streak
     * @param rate the currency rate change event
     * @param streamDirection the direction of change, e.g. rising or falling
     * @param duration the length of time of the current streak
     * @return the message to be sent
     */
    private StreakingChangeMessage createStreakingChangeMessage(CurrencyConversionRate rate, RisingStreamEnum streamDirection, int duration) {
        StreakingChangeMessageAlertType alertType = StreakingChangeMessageAlertType.falling;
        if (streamDirection == RisingStreamEnum.rising) {
            alertType = StreakingChangeMessageAlertType.rising;
        }
        return new StreakingChangeMessage(rate, alertType, duration);
    }
}
