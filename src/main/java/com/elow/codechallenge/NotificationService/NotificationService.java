package com.elow.codechallenge.NotificationService;

/**
 * A service for sending notifications
 */
public class NotificationService {

    /**
     * Sends a notification
     * @param message the message to notify the receivers with
     */
    public void notify(Message message) {
        System.out.println(message);
    }
}
