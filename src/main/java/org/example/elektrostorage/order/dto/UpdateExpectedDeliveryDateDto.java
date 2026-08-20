package org.example.elektrostorage.order.dto;

import java.time.LocalDate;

public record UpdateExpectedDeliveryDateDto(
        LocalDate expectedDeliveryDate
) {
}