package com.restaurant.queue.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements NotificationObserver {
    private static final Logger log = LoggerFactory.getLogger(EmailNotificationObserver.class);

    @Override
    public void update(String phone, String message) {
        log.info("Email notification for contact {}: {}", phone, message);
    }
}