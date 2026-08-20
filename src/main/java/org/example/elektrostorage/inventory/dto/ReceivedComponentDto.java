package org.example.elektrostorage.inventory.dto;

import java.time.LocalDate;

public record ReceivedComponentDto(
        Long componentId,
        String componentName,
        int quantity,
        LocalDate date,
        String countedBy
) {
}