package com.restaurant.queue.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private int partySize;
    private boolean vip;

    public Customer() {
    }

    public Customer(Long id, String name, String phone, int partySize, boolean vip) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.partySize = partySize;
        this.vip = vip;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getPartySize() { return partySize; }
    public boolean isVip() { return vip; }
}