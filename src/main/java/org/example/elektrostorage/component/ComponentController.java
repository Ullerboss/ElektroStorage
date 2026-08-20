package org.example.elektrostorage.component;

import org.example.elektrostorage.component.dto.ComponentDto;
import org.example.elektrostorage.component.dto.CreateComponentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/components")
class ComponentController {

    private final ComponentService componentService;

    ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping
    public ResponseEntity<List<ComponentDto>> getAllComponents() {
        return ResponseEntity.ok(componentService.getAllComponents());
    }

    @PostMapping
    public ResponseEntity<ComponentDto> createComponent(@RequestBody CreateComponentDto request) {
        ComponentDto created = componentService.createComponent(request);
        return ResponseEntity.created(URI.create("/components/" + created.id())).body(created);
    }

    @PatchMapping("/{id}/discontinue")
    public ResponseEntity<ComponentDto> markAsDiscontinued(@PathVariable Long id){
        return ResponseEntity.ok(componentService.markAsDiscontinued(id));
    }
}