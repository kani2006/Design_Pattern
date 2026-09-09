package com.restaurant.queue.service;

import com.restaurant.queue.model.Customer;
import com.restaurant.queue.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) { this.repository = repository; }
    public Customer create(Customer customer) { return repository.save(customer); }
    public Customer get(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Customer not found")); }
    public List<Customer> getAll() { return repository.findAll(); }
}