package com.restaurant.queue.controller;

import com.restaurant.queue.model.Reservation;
import com.restaurant.queue.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation book(@Valid @RequestBody BookReservationRequest request) {
        return service.book(request.customerId(), request.tableId(), request.reservationTime());
    }

    @GetMapping
    public List<Reservation> getAll() { return service.getAll(); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) { service.cancel(id); }

    public record BookReservationRequest(@NotNull Long customerId, @NotNull Long tableId, @NotNull Instant reservationTime) {}
}