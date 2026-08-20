package org.example.elektrostorage.inventory.dto;

import java.time.LocalDate;

public record ReceivedComponentDto(
        String componentName,
        int quantity,
        LocalDate receivedDate
) {
}