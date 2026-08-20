package org.example.elektrostorage.component.dto;

public record ComponentDto(
        Long id,
        String name,
        String supplierName,
        String externalPartNumber,
        boolean discontinued
) {
}