package org.example.elektrostorage.order;

import org.example.elektrostorage.component.Component;
import org.example.elektrostorage.component.ComponentService;
import org.example.elektrostorage.exception.*;
import org.example.elektrostorage.order.dto.*;
import org.example.elektrostorage.supplier.Supplier;
import org.example.elektrostorage.supplier.SupplierService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final SupplierService supplierService;
    private final ComponentService componentService;

    public OrderServiceImpl(OrderRepository orderRepository, SupplierService supplierService, ComponentService componentService) {
        this.orderRepository = orderRepository;
        this.supplierService = supplierService;
        this.componentService = componentService;
    }

    @Override
    public List<Order> getReceivedOrders(){
        return orderRepository.findByReceivedDateIsNotNull();
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public OrderDto createOrder(CreateOrderDto request) {
        Supplier supplier = supplierService.getSupplierById(request.supplierId());
        Order order = new Order(supplier, request.expectedDeliveryDate());
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    public OrderLineDto addOrderLine(Long orderId, AddOrderLineDto request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Ordren med id: " +orderId+ ", blev ikke fundet :/"));

        if (order.isSent()) {
            throw new BadRequestException("Ordren er allerede lagt");
        }

        Component component = componentService.getComponentById(request.componentId());

        if (component.isDiscontinued()) {
            throw new BadRequestException("Komponenten '" +component.getName()+ "' er udgået og kan ikke længere bestilles");
        }

        OrderLine line = new OrderLine(component, request.quantity());
        order.addOrderLine(line);
        orderRepository.save(order);

        return toLineDto(line);
    }

    @Override
    public OrderDto markOrderAsSent(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Ordren med id: " + orderId + ", blev ikke fundet :/"));
        order.markAsSent(LocalDate.now());
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    public OrderDto updateTrackingCode(Long orderId, UpdateTrackingCodeDto request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Ordren med id: " + orderId + ", blev ikke fundet :/"));
        order.setTrackingCode(request.trackingCode());
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }







    private OrderDto toDto(Order order) {
        List<OrderLineDto> lines = order.getOrderLines().stream()
                .map(this::toLineDto)
                .toList();
        return new OrderDto(
                order.getId(),
                order.getSupplier().getName(),
                order.getTrackingCode(),
                order.getExpectedDeliveryDate(),
                order.getSentDate(),
                order.getReceivedDate(),
                lines
        );
    }


    private OrderLineDto toLineDto(OrderLine line) {
        return new OrderLineDto(line.getComponent().getName(), line.getQuantity());
    }

}
