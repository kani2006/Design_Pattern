package com.restaurant.queue.repository;

import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.model.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueRepository extends JpaRepository<QueueEntry, Long> {
    List<QueueEntry> findByStatus(QueueStatus status);
}