package com.restaurant.queue.pattern.state;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;

public class OccupiedState implements TableState {
    public void reserve(RestaurantTable table) { throw new IllegalStateException("Occupied table cannot be reserved"); }
    public void occupy(RestaurantTable table) { table.setStatus(TableStatus.OCCUPIED); }
    public void startCleaning(RestaurantTable table) { table.setStatus(TableStatus.CLEANING); }
    public void makeAvailable(RestaurantTable table) { throw new IllegalStateException("Occupied table must be cleaned first"); }
}