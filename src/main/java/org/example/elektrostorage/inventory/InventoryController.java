package org.example.elektrostorage.inventory;

import org.example.elektrostorage.inventory.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<ReceivedComponentDto>> getReceivedComponents() {
        return ResponseEntity.ok(inventoryService.getReceivedComponents());
    }

    @PostMapping("/counts")
    public ResponseEntity<InventoryCountDto> submitCount (@RequestBody CreateInventoryCountDto request){
        InventoryCountDto created = inventoryService.submitCount(request);
        return ResponseEntity.created(URI.create("/inventory/counts/" + created.id())).body(created);
    }



}
