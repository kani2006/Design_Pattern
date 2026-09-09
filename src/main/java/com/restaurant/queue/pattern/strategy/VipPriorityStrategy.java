package com.restaurant.queue.pattern.strategy;

import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.model.RestaurantTable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("VIP_PRIORITY")
public class VipPriorityStrategy implements TableAllocationStrategy {
    @Override
    public Optional<QueueEntry> selectCustomer(List<QueueEntry> queue, RestaurantTable table) {
        return queue.stream()
                .filter(entry -> entry.getCustomer().getPartySize() <= table.getCapacity())
                .sorted(Comparator.comparing((QueueEntry entry) -> !entry.getCustomer().isVip())
                        .thenComparing(QueueEntry::getJoinedAt))
                .findFirst();
    }
}