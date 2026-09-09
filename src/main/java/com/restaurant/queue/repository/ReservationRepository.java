package com.restaurant.queue.repository;

import com.restaurant.queue.model.Reservation;
import com.restaurant.queue.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatusOrderByReservationTimeAsc(ReservationStatus status);
    boolean existsByTableIdAndStatus(Long tableId, ReservationStatus status);
}