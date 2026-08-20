package org.example.elektrostorage.order;


import org.example.elektrostorage.order.dto.*;
//Explicitte imports da Order bliver blandet/ mistforstået med junits order
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

    @Autowired
    MockMvcTester mvc;

    @MockitoBean
    OrderService service;

    @Test
    @DisplayName("GET /orders - skal returnere en liste af ordrer")
    @Order(1)
    void shouldReturnOrdersList() {
        OrderDto order = new OrderDto(1L, "Elfa", null, LocalDate.parse("2026-03-15"), null, null, List.of());
        when(service.getAllOrders()).thenReturn(List.of(order));

        var request = mvc.get().uri("/orders");

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(org.assertj.core.api.InstanceOfAssertFactories.list(OrderDto.class))
                .satisfies(orders -> {
                    assertThat(orders).hasSize(1);
                    assertThat(orders.getFirst().supplierName()).isEqualTo("Elfa");
                });
    }

    @Test
    @DisplayName("POST /orders - skal oprette en ny ordre")
    @Order(2)
    void shouldCreateOrder() {
        OrderDto created = new OrderDto(1L, "Elfa", null, LocalDate.parse("2026-03-15"), null, null, List.of());
        when(service.createOrder(any(CreateOrderDto.class))).thenReturn(created);

        String requestBody = """
                {
                    "supplierId": 1,
                    "expectedDeliveryDate": "2026-03-15"
                }
                """;

        var request = mvc.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        assertThat(request)
                .hasStatus(HttpStatus.CREATED)
                .hasHeader(HttpHeaders.LOCATION, "/orders/1")
                .bodyJson()
                .convertTo(OrderDto.class)
                .satisfies(o -> assertThat(o.supplierName()).isEqualTo("Elfa"));
    }

    @Test
    @DisplayName("POST /orders/{id}/lines - skal tilføje en linje til ordren")
    @Order(3)
    void shouldAddOrderLine() {
        Long orderId = 1L;
        OrderLineDto created = new OrderLineDto("LED 5mm rød", 10);
        when(service.addOrderLine(eq(orderId), any(AddOrderLineDto.class))).thenReturn(created);

        String requestBody = """
                {
                    "componentId": 1,
                    "quantity": 10
                }
                """;

        var request = mvc.post()
                .uri("/orders/{id}/lines", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        assertThat(request)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(OrderLineDto.class)
                .satisfies(line -> {
                    assertThat(line.componentName()).isEqualTo("LED 5mm rød");
                    assertThat(line.quantity()).isEqualTo(10);
                });
    }

    @Test
    @DisplayName("PATCH /orders/{id}/send - skal markere ordren som sendt")
    @Order(4)
    void shouldMarkOrderAsSent() {
        Long orderId = 1L;
        OrderDto sent = new OrderDto(orderId, "Elfa", null, LocalDate.parse("2026-03-15"), LocalDate.now(), null, List.of());
        when(service.markOrderAsSent(orderId)).thenReturn(sent);

        var request = mvc.patch()
                .uri("/orders/{id}/send", orderId)
                .contentType(MediaType.APPLICATION_JSON);

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(OrderDto.class)
                .satisfies(o -> assertThat(o.sentDate()).isNotNull());
    }

    @Test
    @DisplayName("PATCH /orders/{id}/tracking - skal opdatere tracking-koden")
    @Order(5)
    void shouldUpdateTrackingCode() {
        Long orderId = 1L;
        OrderDto updated = new OrderDto(orderId, "Elfa", "TRACK-123", LocalDate.parse("2026-03-15"), null, null, List.of());
        when(service.updateTrackingCode(eq(orderId), any(UpdateTrackingCodeDto.class))).thenReturn(updated);

        String requestBody = """
                {
                    "trackingCode": "TRACK-123"
                }
                """;

        var request = mvc.patch()
                .uri("/orders/{id}/tracking", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(OrderDto.class)
                .satisfies(o -> assertThat(o.trackingCode()).isEqualTo("TRACK-123"));
    }
}