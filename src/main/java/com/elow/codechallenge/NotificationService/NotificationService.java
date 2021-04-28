package com.elow.codechallenge.NotificationService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;



/**
 * A service for sending notifications
 */
public class NotificationService {
    public static final String ACCOUNT_SID = "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String AUTH_TOKEN = "2cdXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX5c";

    /**
     * Sends a notification
     * @param message the message to notify the receivers with
     */
    public void notify(com.elow.codechallenge.NotificationService.Message message) {
        sendSms(message);
    }

    private void sendSms(com.elow.codechallenge.NotificationService.Message message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message smsmessage = Message.creator(new PhoneNumber("+14157XXXXXX9"),
                new PhoneNumber("+14156XXXXXX5"),
                message.toString()).create();

        System.out.println(smsmessage.getSid());
    }
}
