package com.restaurant.queue.service;

import com.restaurant.queue.model.Customer;
import com.restaurant.queue.model.Reservation;
import com.restaurant.queue.model.ReservationStatus;
import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;
import com.restaurant.queue.pattern.observer.NotificationSubject;
import com.restaurant.queue.repository.ReservationRepository;
import com.restaurant.queue.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final CustomerService customerService;
    private final NotificationSubject notificationSubject;

    public ReservationService(ReservationRepository reservationRepository, TableRepository tableRepository,
                               CustomerService customerService, NotificationSubject notificationSubject) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.customerService = customerService;
        this.notificationSubject = notificationSubject;
    }

    @Transactional
    public Reservation book(Long customerId, Long tableId, Instant reservationTime) {
        Customer customer = customerService.get(customerId);
        RestaurantTable table = tableRepository.findLockedById(tableId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Table not found"));
        if (table.getStatus() != TableStatus.AVAILABLE || reservationRepository.existsByTableIdAndStatus(tableId, ReservationStatus.CONFIRMED)) {
            throw new ResponseStatusException(CONFLICT, "Table is already booked or unavailable");
        }
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
        Reservation reservation = reservationRepository.save(new Reservation(customer, table, reservationTime));
        notificationSubject.notifyObservers(customer.getPhone(), "Reservation confirmed for table " + table.getTableNumber() + ".");
        return reservation;
    }

    public List<Reservation> getAll() { return reservationRepository.findAll(); }

    @Transactional
    public void cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reservation not found"));
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new ResponseStatusException(CONFLICT, "Reservation is not active");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        RestaurantTable table = tableRepository.findLockedById(reservation.getTable().getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Table not found"));
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
        reservationRepository.save(reservation);
    }
}