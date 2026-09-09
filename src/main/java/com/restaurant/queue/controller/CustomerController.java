package com.restaurant.queue.controller;

import com.restaurant.queue.model.Customer;
import com.restaurant.queue.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody CreateCustomerRequest request) {
        return service.create(new Customer(null, request.name(), request.phone(), request.partySize(), request.vip()));
    }
    @GetMapping
    public List<Customer> getAll() { return service.getAll(); }
    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) { return service.get(id); }

    public record CreateCustomerRequest(@NotBlank String name, @NotBlank @Pattern(regexp = "[+0-9 ()-]{7,20}") String phone,
                                        @Min(1) int partySize, boolean vip) {}
}