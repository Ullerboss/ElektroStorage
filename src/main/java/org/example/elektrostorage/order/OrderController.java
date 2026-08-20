package org.example.elektrostorage.order;

import org.example.elektrostorage.order.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDto request) {
        OrderDto created = orderService.createOrder(request);
        return ResponseEntity.created(URI.create("/orders/" + created.id())).body(created);
    }

    @PostMapping("/{id}/lines")
    public ResponseEntity<OrderLineDto> addOrderLine(@PathVariable Long id, @RequestBody AddOrderLineDto request) {
        OrderLineDto created = orderService.addOrderLine(id, request);
        return ResponseEntity.created(URI.create("/orders/" + id + "/lines")).body(created);
    }

    @PatchMapping("/{id}/send")
    public ResponseEntity<OrderDto> markOrderAsSent(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markOrderAsSent(id));
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<OrderDto> updateTrackingCode(@PathVariable Long id, @RequestBody UpdateTrackingCodeDto request) {
        return ResponseEntity.ok(orderService.updateTrackingCode(id, request));
    }

    @PatchMapping("/{id}/expected-delivery")
    public ResponseEntity<OrderDto> updateExpectedDeliveryDate(@PathVariable Long id, @RequestBody UpdateExpectedDeliveryDateDto request) {
        return ResponseEntity.ok(orderService.updateExpectedDeliveryDate(id, request));
    }

    @PatchMapping("/{id}/receive")
    public ResponseEntity<OrderDto> markOrderAsReceived(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markOrderAsReceived(id));
    }
}