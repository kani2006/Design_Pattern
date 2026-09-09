package com.restaurant.queue.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;
    private Instant reservationTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    protected Reservation() {
    }

    public Reservation(Customer customer, RestaurantTable table, Instant reservationTime) {
        this.customer = customer;
        this.table = table;
        this.reservationTime = reservationTime;
        this.status = ReservationStatus.CONFIRMED;
    }

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public RestaurantTable getTable() { return table; }
    public Instant getReservationTime() { return reservationTime; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}