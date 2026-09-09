package com.restaurant.queue.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int tableNumber;
    private int capacity;
    @Enumerated(EnumType.STRING)
    private TableType type;
    @Enumerated(EnumType.STRING)
    private TableStatus status;

    public RestaurantTable() {
    }

    public RestaurantTable(Long id, int tableNumber, int capacity, TableType type) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.type = type;
        this.status = TableStatus.AVAILABLE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getTableNumber() { return tableNumber; }
    public int getCapacity() { return capacity; }
    public TableType getType() { return type; }
    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}