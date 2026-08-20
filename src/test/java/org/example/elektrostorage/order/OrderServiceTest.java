package org.example.elektrostorage.order;

import org.example.elektrostorage.component.Component;
import org.example.elektrostorage.component.ComponentService;
import org.example.elektrostorage.exception.BadRequestException;
import org.example.elektrostorage.order.dto.AddOrderLineDto;
import org.example.elektrostorage.supplier.Supplier;
import org.example.elektrostorage.supplier.SupplierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    SupplierService supplierService;

    @Mock
    ComponentService componentService;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    @DisplayName("addOrderLine - skal kaste BadRequestException hvis ordren allerede er sendt")
    void shouldThrowBadRequestWhenOrderIsAlreadySent(){
        Long orderId = 1L;
        Supplier supplier = new Supplier("jem&Fix", "Mindehøjvej 3");
        Order order = new Order(supplier, LocalDate.parse("2026-05-05"));
        order.markAsSent(LocalDate.now());

        AddOrderLineDto request = new AddOrderLineDto(3L,12);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() ->orderService.addOrderLine(orderId,request))
                .isInstanceOf(BadRequestException.class);
    }



}
