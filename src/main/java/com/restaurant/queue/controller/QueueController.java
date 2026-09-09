package com.restaurant.queue.controller;

import com.restaurant.queue.model.QueueEntry;
import com.restaurant.queue.service.QueueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    private final QueueService service;
    public QueueController(QueueService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QueueEntry join(@Valid @RequestBody JoinQueueRequest request) { return service.join(request.customerId()); }
    @GetMapping
    public List<QueueEntry> getAll() { return service.getAll(); }
    @GetMapping("/{id}")
    public QueueEntry get(@PathVariable Long id) { return service.get(id); }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) { service.cancel(id); }
    @PostMapping("/allocate")
    public QueueEntry allocate(@Valid @RequestBody AllocateRequest request) { return service.allocate(request.tableId(), request.strategy()); }

    public record JoinQueueRequest(@NotNull Long customerId) {}
    public record AllocateRequest(@NotNull Long tableId, @NotBlank String strategy) {}
}