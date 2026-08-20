package org.example.elektrostorage.assembly.dto;

import java.util.List;

public record AssemblyListDto(
        Long id,
        String resultComponentName,
        List<AssemblyItemDto> items
) {
}