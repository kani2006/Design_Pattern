package com.restaurant.queue.pattern.state;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;

public class ReservedState implements TableState {
    public void reserve(RestaurantTable table) { table.setStatus(TableStatus.RESERVED); }
    public void occupy(RestaurantTable table) { table.setStatus(TableStatus.OCCUPIED); }
    public void startCleaning(RestaurantTable table) { throw new IllegalStateException("Reserved table cannot start cleaning"); }
    public void makeAvailable(RestaurantTable table) { table.setStatus(TableStatus.AVAILABLE); }
}