package com.elow.codechallenge.Alerts;

import java.util.ArrayList;
import java.util.List;

public class AlertServiceFactory {
    public List<AlertService> createAlertServices() {
        List<AlertService> services = new ArrayList<>();

        services.add(new SpotChangeAlertService());
        services.add(new StreakingChangeAlertService(15 * 60));

        return services;
    }
}
