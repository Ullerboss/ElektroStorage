package org.example.elektrostorage.order.dto;

public record OrderLineDto(
        String componentName,
        int quantity
) {
}