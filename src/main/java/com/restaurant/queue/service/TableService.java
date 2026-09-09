package com.restaurant.queue.service;

import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;
import com.restaurant.queue.model.TableType;
import com.restaurant.queue.pattern.factory.TableFactory;
import com.restaurant.queue.pattern.state.TableStateContext;
import com.restaurant.queue.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TableService {
    private final TableRepository repository;
    private final TableFactory factory;
    private final TableStateContext stateContext;

    public TableService(TableRepository repository, TableFactory factory, TableStateContext stateContext) {
        this.repository = repository;
        this.factory = factory;
        this.stateContext = stateContext;
    }

    public RestaurantTable create(int tableNumber, TableType type) {
        return repository.save(factory.createTable(null, tableNumber, type));
    }

    public List<RestaurantTable> getAll() { return repository.findAll(); }
    public List<RestaurantTable> getAvailable() { return repository.findByStatus(TableStatus.AVAILABLE); }

    public RestaurantTable updateStatus(Long id, TableStatus status) {
        RestaurantTable table = get(id);
        try {
            stateContext.transition(table, status);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(BAD_REQUEST, exception.getMessage(), exception);
        }
        return repository.save(table);
    }

    public RestaurantTable get(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Table not found")); }

    @Transactional
    public RestaurantTable getLocked(Long id) { return repository.findLockedById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Table not found")); }
}