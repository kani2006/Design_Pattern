package com.restaurant.queue.controller;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;
import com.restaurant.queue.model.TableType;
import com.restaurant.queue.service.TableService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService service;
    public TableController(TableService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantTable create(@Valid @RequestBody CreateTableRequest request) { return service.create(request.tableNumber(), request.type()); }
    @GetMapping
    public List<RestaurantTable> getAll() { return service.getAll(); }
    @GetMapping("/available")
    public List<RestaurantTable> getAvailable() { return service.getAvailable(); }
    @PutMapping("/{id}/status")
    public RestaurantTable updateStatus(@PathVariable Long id, @RequestParam TableStatus status) { return service.updateStatus(id, status); }

    public record CreateTableRequest(@Min(1) int tableNumber, @NotNull TableType type) {}
}