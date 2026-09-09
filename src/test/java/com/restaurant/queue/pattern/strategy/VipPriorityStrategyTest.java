package com.restaurant.queue.pattern.strategy;

import com.restaurant.queue.model.Customer;
import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VipPriorityStrategyTest {
    @Test
    void selectsVipBeforeEarlierRegularCustomer() {
        RestaurantTable table = new RestaurantTable(1L, 1, 4, TableType.FOUR_SEATER);
        QueueEntry regular = new QueueEntry(1L, new Customer(1L, "Regular", "1111111", 2, false));
        QueueEntry vip = new QueueEntry(2L, new Customer(2L, "VIP", "2222222", 2, true));

        assertEquals(vip, new VipPriorityStrategy().selectCustomer(List.of(regular, vip), table).orElseThrow());
    }
}