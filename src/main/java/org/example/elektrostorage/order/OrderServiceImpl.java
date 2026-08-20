package org.example.elektrostorage.order;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getReceivedOrders(){
        return orderRepository.findByReceivedDateIsNotNull();
    }

}
