package org.example.elektrostorage.order;

import org.example.elektrostorage.order.dto.*;

import java.util.List;

public interface OrderService {
    List<Order> getReceivedOrders();
    List<OrderDto> getAllOrders();
    OrderDto createOrder(CreateOrderDto request);
    OrderLineDto addOrderLine(Long orderId, AddOrderLineDto request);
    OrderDto markOrderAsSent(Long orderId);
    OrderDto updateTrackingCode(Long orderId, UpdateTrackingCodeDto request);
    OrderDto updateExpectedDeliveryDate(Long orderId, UpdateExpectedDeliveryDateDto request);
    OrderDto markOrderAsReceived(Long orderId);
}
