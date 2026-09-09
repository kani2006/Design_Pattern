package com.restaurant.queue.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(SmsNotificationObserver.class);

    @Override
    public void update(String phone, String message) {
        log.info("SMS to {}: {}", phone, message);
    }
}