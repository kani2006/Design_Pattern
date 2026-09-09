package com.restaurant.queue.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.Instant;

@Entity
public class QueueEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    private Instant joinedAt;
    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    protected QueueEntry() {
    }

    public QueueEntry(Long id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.joinedAt = Instant.now();
        this.status = QueueStatus.WAITING;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public Instant getJoinedAt() { return joinedAt; }
    public QueueStatus getStatus() { return status; }
    public void setStatus(QueueStatus status) { this.status = status; }
}