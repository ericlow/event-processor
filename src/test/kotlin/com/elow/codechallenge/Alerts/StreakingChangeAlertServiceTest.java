package com.elow.codechallenge.Alerts;

import com.elow.codechallenge.CurrencyConversionRateHistory;
import com.elow.codechallenge.NotificationService.StreakingChangeMessage;
import com.elow.codechallenge.input.CurrencyConversionRate;
import mockit.Deencapsulation;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class StreakingChangeAlertServiceTest {

    private CurrencyConversionRate createRateEvent(double rate) {
        return new CurrencyConversionRate(Instant.now(), "CNYAUD", rate);
    }

    @Test
    public void createStreakingChangeMessage_should_create_rising_message_if_rising() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRate rate = createRateEvent(.34);
        StreakingChangeAlertService.RisingStreamEnum streamDirection = StreakingChangeAlertService.RisingStreamEnum.rising;
        int duration = 100;

        // act
        StreakingChangeMessage message = Deencapsulation.invoke(service, "createStreakingChangeMessage", rate, streamDirection, duration);

        // assert
        assertNotNull(message);
        assertEquals("rising", Deencapsulation.getField(message, "alertType").toString());
        assertEquals(100, (Integer)Deencapsulation.getField(message, "duration"));
    }

    @Test
    public void isAlertableEvent_should_return_true_when_60_after_max_streak () {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        int streakMaxSec = 300;
        int streakCounter = 360;

        // act
        boolean isAlertableEvent = Deencapsulation.invoke(service, "isAlertableEvent", streakMaxSec, streakCounter);

        // assert
        assertTrue(isAlertableEvent);
    }

    @Test
    public void isAlertableEvent_should_return_false_when_61_after_max_streak () {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        int streakMaxSec = 300;
        int streakCounter = 361;

        // act
        boolean isAlertableEvent = Deencapsulation.invoke(service, "isAlertableEvent", streakMaxSec, streakCounter);

        // assert
        assertFalse(isAlertableEvent);
    }

    @Test
    public void isAlertableStreak_should_return_true_when_after_max_streak() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        int streakMaxSec = 300;
        int streakCounter = 301;

        // act
        boolean isAlertableEvent = Deencapsulation.invoke(service, "isAlertableStreak", streakMaxSec, streakCounter);

        // assert
        assertTrue(isAlertableEvent);
    }

    @Test
    public void isAlertableStreak_should_return_false_when_before_max_streak() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        int streakMaxSec = 300;
        int streakCounter = 299;

        // act
        boolean isAlertableEvent = Deencapsulation.invoke(service, "isAlertableStreak", streakMaxSec, streakCounter);

        // assert
        assertFalse(isAlertableEvent);
    }

    @Test
    public void isRising_should_return_true_when_rates_rising() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        double lastRate = .1;
        double newRate = .2;

        // act
        boolean isRising = Deencapsulation.invoke(service, "isRising", lastRate, newRate);

        // assert
        assertTrue(isRising);
    }

    @Test
    public void isRising_should_return_false_when_rates_rising() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        double lastRate = .2;
        double newRate = .1;

        // act
        boolean isRising = Deencapsulation.invoke(service, "isRising", lastRate, newRate);

        // assert
        assertFalse(isRising);
    }

    @Test
    public void isFalling_should_return_false_when_rates_rising() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        double lastRate = .1;
        double newRate = .2;

        // act
        boolean isFalling = Deencapsulation.invoke(service, "isFalling", lastRate, newRate);

        // assert
        assertFalse(isFalling);
    }

    @Test
    public void isFalling_should_return_true_when_rates_falling() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        double lastRate = .2;
        double newRate = .1;

        // act
        boolean isFalling = Deencapsulation.invoke(service, "isFalling", lastRate, newRate);

        // assert
        assertTrue(isFalling);
    }

    @Test
    public void reviewHistory_should_set_rising_streakType_and_increment_counter_when_no_streak_and_rate_rising() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(createRateEvent(.34));
        service.reviewHistory(history);
        history.add(createRateEvent(.35));

        // act
        service.reviewHistory(history);

        // assert
        assertEquals(StreakingChangeAlertService.RisingStreamEnum.rising, Deencapsulation.getField(service, "streakType"));
        assertEquals(1, (Integer)Deencapsulation.getField(service, "streakCounter"));
    }

    @Test
    public void reviewHistory_should_set_falling_streakType_and_increment_counter_when_no_streak_and_rate_falling() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(createRateEvent(.34));
        service.reviewHistory(history);
        history.add(createRateEvent(.33));

        // act
        service.reviewHistory(history);

        // assert
        assertEquals(StreakingChangeAlertService.RisingStreamEnum.falling, Deencapsulation.getField(service, "streakType"));
        assertEquals(1, (Integer)Deencapsulation.getField(service, "streakCounter"));
    }

    @Test
    public void reviewHistory_should_set_steady_streakType_and_0_counter_when_no_streak_and_rate_steady() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(createRateEvent(.34));
        service.reviewHistory(history);
        history.add(createRateEvent(.34));

        // act
        service.reviewHistory(history);

        // assert
        assertEquals(StreakingChangeAlertService.RisingStreamEnum.steady, Deencapsulation.getField(service, "streakType"));
        assertEquals(0, (Integer)Deencapsulation.getField(service, "streakCounter"));
    }

    @Test
    public void reviewHistory_should_set_steady_streakType_and_0_counter_when_streak_broken() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(createRateEvent(.34));
        service.reviewHistory(history);
        history.add(createRateEvent(.35));
        service.reviewHistory(history);
        history.add(createRateEvent(.34));

        // act
        service.reviewHistory(history);

        // assert
        assertEquals(StreakingChangeAlertService.RisingStreamEnum.steady, Deencapsulation.getField(service, "streakType"));
        assertEquals(0, (Integer)Deencapsulation.getField(service, "streakCounter"));
    }

    @Test
    public void reviewHistory_should_increment_counter_when_rising_in_streak() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(createRateEvent(.34));
        service.reviewHistory(history);
        history.add(createRateEvent(.35));
        service.reviewHistory(history);
        history.add(createRateEvent(.36));

        // act
        service.reviewHistory(history);

        // assert
        assertEquals(StreakingChangeAlertService.RisingStreamEnum.rising, Deencapsulation.getField(service, "streakType"));
        assertEquals(2, (Integer)Deencapsulation.getField(service, "streakCounter"));
    }

    @Test
    public void reviewHistory_should_increment_counter_when_falling_in_streak() {
        // arrange
        StreakingChangeAlertService service = new StreakingChangeAlertService(3);
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(createRateEvent(.34));
        service.reviewHistory(history);
        history.add(createRateEvent(.33));
        service.reviewHistory(history);
        history.add(createRateEvent(.32));

        // act
        service.reviewHistory(history);

        // assert
        assertEquals(StreakingChangeAlertService.RisingStreamEnum.falling, Deencapsulation.getField(service, "streakType"));
        assertEquals(2, (Integer)Deencapsulation.getField(service, "streakCounter"));
    }
}
