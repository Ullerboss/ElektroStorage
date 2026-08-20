package org.example.elektrostorage.assembly;

import org.example.elektrostorage.assembly.dto.AssemblyListDto;

import java.util.List;

public interface AssemblyService {
    List<AssemblyListDto> getAllAssemblyLists();
}