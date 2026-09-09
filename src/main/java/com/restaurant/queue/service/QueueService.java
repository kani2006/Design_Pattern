package com.restaurant.queue.service;

import com.restaurant.queue.model.Customer;
import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.model.QueueStatus;
import com.restaurant.queue.model.RestaurantTable;
import com.restaurant.queue.model.TableStatus;
import com.restaurant.queue.pattern.observer.NotificationSubject;
import com.restaurant.queue.pattern.strategy.TableAllocationStrategy;
import com.restaurant.queue.repository.QueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class QueueService {
    private final QueueRepository repository;
    private final CustomerService customerService;
    private final TableService tableService;
    private final NotificationSubject notificationSubject;
    private final Map<String, TableAllocationStrategy> strategies;

    public QueueService(QueueRepository repository, CustomerService customerService, TableService tableService,
                        NotificationSubject notificationSubject, Map<String, TableAllocationStrategy> strategies) {
        this.repository = repository;
        this.customerService = customerService;
        this.tableService = tableService;
        this.notificationSubject = notificationSubject;
        this.strategies = strategies;
    }

    public QueueEntry join(Long customerId) {
        Customer customer = customerService.get(customerId);
        return repository.save(new QueueEntry(null, customer));
    }

    public List<QueueEntry> getAll() { return repository.findAll(); }
    public QueueEntry get(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Queue entry not found")); }

    @Transactional
    public QueueEntry allocate(Long tableId, String strategyName) {
        RestaurantTable table = tableService.getLocked(tableId);
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new ResponseStatusException(BAD_REQUEST, "Table is not available");
        }
        TableAllocationStrategy strategy = Optional.ofNullable(strategies.get(strategyName.toUpperCase()))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Unknown strategy: " + strategyName));
        QueueEntry entry = strategy.selectCustomer(repository.findByStatus(QueueStatus.WAITING), table)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "No suitable customer is waiting"));
        entry.setStatus(QueueStatus.ALLOCATED);
        tableService.updateStatus(tableId, TableStatus.OCCUPIED);
        notificationSubject.notifyObservers(entry.getCustomer().getPhone(),
                "Your table " + table.getTableNumber() + " is ready.");
        return repository.save(entry);
    }

    public void cancel(Long id) {
        QueueEntry entry = get(id);
        if (entry.getStatus() != QueueStatus.WAITING) {
            throw new ResponseStatusException(BAD_REQUEST, "Only waiting entries can be cancelled");
        }
        entry.setStatus(QueueStatus.CANCELLED);
        repository.save(entry);
    }
}