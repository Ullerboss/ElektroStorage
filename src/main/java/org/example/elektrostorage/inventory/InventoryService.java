package org.example.elektrostorage.inventory;

import org.example.elektrostorage.inventory.dto.*;

import java.util.List;

public interface InventoryService {
    InventoryCountDto submitCount(CreateInventoryCountDto request);
    List<ReceivedComponentDto> getReceivedComponents();
}
