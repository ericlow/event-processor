package com.elow.codechallenge;

import com.elow.codechallenge.Alerts.AlertService;
import com.elow.codechallenge.input.CurrencyConversionRate;
import mockit.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EventProcessorTest {

    @Test
    void insertRateEvent_should_create_conversionRateHistoryMap_for_new_currency_pairs() {
        // arrange
        EventProcessor ep = new EventProcessor();
        Map<String, CurrencyConversionRateHistory> conversionRateHistoryMap = new HashMap<>();
        CurrencyConversionRate currencyConversionRate = new CurrencyConversionRate(Instant.now(), "CNYAUD", .5d);

        // act
        CurrencyConversionRateHistory history = Deencapsulation.invoke(ep, "insertRateEvent", conversionRateHistoryMap, currencyConversionRate);

        // assert
        assertNotNull(history);
        assertEquals(1, conversionRateHistoryMap.size());
    }

    @Test
    void checkAlerts_should_create_alertServiceMap_for_new_currency_pair() {
        // arrange
        EventProcessor ep = new EventProcessor();
        Map<String, List<AlertService>> alertServiceMap = new HashMap<>();
        CurrencyConversionRateHistory history = new CurrencyConversionRateHistory("CNYAUD");
        history.add(new CurrencyConversionRate(Instant.now(), "CNYAUD", .5d));

        // act
        Deencapsulation.invoke(ep, "checkAlerts", alertServiceMap, history);

        // assert
        assertNotNull(alertServiceMap.containsKey("CNYAUD"));
        assertNotNull(alertServiceMap.get("CNYAUD"));
    }
}