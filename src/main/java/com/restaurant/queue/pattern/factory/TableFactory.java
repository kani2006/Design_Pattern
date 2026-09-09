package com.restaurant.queue.pattern.factory;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableType;
import org.springframework.stereotype.Component;

@Component
public class TableFactory {

    public RestaurantTable createTable(Long id, int tableNumber, TableType type) {
        int capacity = switch (type) {
            case TWO_SEATER -> 2;
            case FOUR_SEATER -> 4;
            case SIX_SEATER -> 6;
            case VIP -> 8;
        };
        return new RestaurantTable(id, tableNumber, capacity, type);
    }
}