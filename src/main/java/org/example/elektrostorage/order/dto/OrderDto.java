package org.example.elektrostorage.order.dto;

import java.time.LocalDate;
import java.util.List;

public record OrderDto(
        Long id,
        String supplierName,
        String trackingCode,
        LocalDate expectedDeliveryDate,
        LocalDate sentDate,
        LocalDate receivedDate,
        List<OrderLineDto> orderLines
) {
}