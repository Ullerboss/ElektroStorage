package org.example.elektrostorage.component.dto;

public record CreateComponentDto(
        String name,
        Long supplierId,
        String externalPartNumber
) {
}