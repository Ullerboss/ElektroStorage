package org.example.elektrostorage.order.dto;

import java.time.LocalDate;

public record CreateOrderDto(
        Long supplierId,
        LocalDate expectedDeliveryDate
) {
}