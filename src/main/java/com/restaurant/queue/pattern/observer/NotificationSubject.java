package com.restaurant.queue.pattern.observer;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationSubject {
    private final List<NotificationObserver> observers;

    public NotificationSubject(List<NotificationObserver> observers) {
        this.observers = observers;
    }

    public void notifyObservers(String phone, String message) {
        observers.forEach(observer -> observer.update(phone, message));
    }
}