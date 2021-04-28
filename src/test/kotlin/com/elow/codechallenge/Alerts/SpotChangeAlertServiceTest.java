package com.elow.codechallenge.Alerts;

import com.elow.codechallenge.CurrencyConversionRateHistory;
import com.elow.codechallenge.MovingAverage;
import com.elow.codechallenge.input.CurrencyConversionRate;
import mockit.Deencapsulation;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpotChangeAlertServiceTest {

    // jmockit MockUps may be imcompatible with junit5 or there might be issues with gradle
    // https://github.com/junit-team/junit5/issues/1792
    // roll our own stub instead
    class MovingAverageMock extends MovingAverage {
        double average;
        public MovingAverageMock(int size) {
            super(size);
        }

        public MovingAverageMock(int size, double average) {
            super(size);
            this.average = average;
        }

        public Optional<Double> getAverage() {
            return Optional.of(average);
        }
    }

    private CurrencyConversionRate createRateEvent(double rate) {
        return new CurrencyConversionRate(Instant.now(), "CNYAUD", rate);
    }

    @NotNull
    private CurrencyConversionRateHistory createMockedRateHistory(double rate) {
        return new CurrencyConversionRateHistory("CNYAUD", new MovingAverageMock(5, rate));
    }

    @Test
    public void isSpotAlert_should_return_false_when_too_little_data() {
        // arrange
        SpotChangeAlertService service = new SpotChangeAlertService();
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");

        // act
        boolean isSpotAlert = Deencapsulation.invoke(service, "isSpotAlert", history);

        // assert
        assertFalse(isSpotAlert);
    }

    @Test
    public void isSpotAlert_should_return_false_when_difference_does_not_exceed_threshold() {
        // arrange
        SpotChangeAlertService service = new SpotChangeAlertService();
        CurrencyConversionRateHistory history = createMockedRateHistory(.3);
        history.add(createRateEvent(.3));
        // act
        boolean isSpotAlert = Deencapsulation.invoke(service, "isSpotAlert", history);

        // assert
        assertFalse(isSpotAlert);

    }

    @Test
    public void isSpotAlert_should_return_false_when_difference_exceeds_threshold() {
        // arrange
        SpotChangeAlertService service = new SpotChangeAlertService();
        CurrencyConversionRateHistory history = createMockedRateHistory(.5);
        history.add(createRateEvent(.3));

        // act
        boolean isSpotAlert = Deencapsulation.invoke(service, "isSpotAlert", history);

        // assert
        assertTrue(isSpotAlert);
    }

}