package org.example.elektrostorage.order.dto;

public record AddOrderLineDto(
        Long componentId,
        int quantity
) {
}