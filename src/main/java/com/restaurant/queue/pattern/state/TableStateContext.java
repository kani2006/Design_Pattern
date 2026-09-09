package com.restaurant.queue.pattern.state;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;
import org.springframework.stereotype.Component;

@Component
public class TableStateContext {
    public void transition(RestaurantTable table, TableStatus target) {
        TableState state = switch (table.getStatus()) {
            case AVAILABLE -> new AvailableState();
            case RESERVED -> new ReservedState();
            case OCCUPIED -> new OccupiedState();
            case CLEANING -> new CleaningState();
        };
        switch (target) {
            case AVAILABLE -> state.makeAvailable(table);
            case RESERVED -> state.reserve(table);
            case OCCUPIED -> state.occupy(table);
            case CLEANING -> state.startCleaning(table);
        }
    }
}