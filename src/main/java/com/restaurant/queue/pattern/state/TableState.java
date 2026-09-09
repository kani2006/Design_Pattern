package com.restaurant.queue.pattern.state;

import com.restaurant.queue.model.RestaurantTable;

public interface TableState {
    void reserve(RestaurantTable table);
    void occupy(RestaurantTable table);
    void startCleaning(RestaurantTable table);
    void makeAvailable(RestaurantTable table);
}