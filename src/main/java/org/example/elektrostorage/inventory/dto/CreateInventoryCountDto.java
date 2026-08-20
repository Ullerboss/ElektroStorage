package org.example.elektrostorage.inventory.dto;

public record CreateInventoryCountDto(
    Long componentId,
    int countedQuantity,
    String countedBy
){
}
