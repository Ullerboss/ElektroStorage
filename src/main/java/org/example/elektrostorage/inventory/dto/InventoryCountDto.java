package org.example.elektrostorage.inventory.dto;

import java.time.LocalDateTime;

public record InventoryCountDto(
        Long id,
        String componentName,
        int countedQuantity,
        String countedBy,
        LocalDateTime countedAt
) {
}
