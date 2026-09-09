package com.restaurant.queue.pattern.state;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;

public class CleaningState implements TableState {
    public void reserve(RestaurantTable table) { throw new IllegalStateException("Cleaning table cannot be reserved"); }
    public void occupy(RestaurantTable table) { throw new IllegalStateException("Cleaning table cannot be occupied"); }
    public void startCleaning(RestaurantTable table) { table.setStatus(TableStatus.CLEANING); }
    public void makeAvailable(RestaurantTable table) { table.setStatus(TableStatus.AVAILABLE); }
}