package com.restaurant.queue.pattern.strategy;

import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.model.RestaurantTable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("SMALLEST_SUITABLE")
public class SmallestSuitableTableStrategy implements TableAllocationStrategy {
    @Override
    public Optional<QueueEntry> selectCustomer(List<QueueEntry> queue, RestaurantTable table) {
        return queue.stream()
                .filter(entry -> entry.getCustomer().getPartySize() <= table.getCapacity())
                .min(Comparator.comparingInt(entry -> entry.getCustomer().getPartySize()));
    }
}