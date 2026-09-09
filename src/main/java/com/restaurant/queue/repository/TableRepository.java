package com.restaurant.queue.repository;

import com.restaurant.queue.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import com.restaurant.queue.model.TableStatus;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByStatus(TableStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select table from RestaurantTable table where table.id = :id")
    Optional<RestaurantTable> findLockedById(@Param("id") Long id);
}