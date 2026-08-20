package org.example.elektrostorage.assembly;

import org.example.elektrostorage.assembly.dto.AssemblyItemDto;
import org.example.elektrostorage.assembly.dto.AssemblyListDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class AssemblyServiceImpl implements AssemblyService {

    private final AssemblyListRepository assemblyListRepository;

    AssemblyServiceImpl(AssemblyListRepository assemblyListRepository) {
        this.assemblyListRepository = assemblyListRepository;
    }

    @Override
    public List<AssemblyListDto> getAllAssemblyLists() {
        return assemblyListRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private AssemblyListDto toDto(AssemblyList assemblyList) {
        List<AssemblyItemDto> items = assemblyList.getItems().stream()
                .map(item -> new AssemblyItemDto(item.getComponent().getName(), item.getQuantity()))
                .toList();
        return new AssemblyListDto(
                assemblyList.getId(),
                assemblyList.getResultComponent().getName(),
                items
        );
    }
}