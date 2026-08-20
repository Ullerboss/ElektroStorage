package org.example.elektrostorage.assembly;

import org.example.elektrostorage.assembly.dto.AssemblyListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assemblies")
class AssemblyController {

    private final AssemblyService assemblyService;

    AssemblyController(AssemblyService assemblyService) {
        this.assemblyService = assemblyService;
    }

    @GetMapping
    public ResponseEntity<List<AssemblyListDto>> getAllAssemblyLists() {
        return ResponseEntity.ok(assemblyService.getAllAssemblyLists());
    }
}