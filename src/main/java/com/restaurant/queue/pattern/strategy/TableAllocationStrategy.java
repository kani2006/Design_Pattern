package com.restaurant.queue.pattern.strategy;

import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.model.RestaurantTable;

import java.util.List;
import java.util.Optional;

public interface TableAllocationStrategy {
    Optional<QueueEntry> selectCustomer(List<QueueEntry> queue, RestaurantTable table);
}