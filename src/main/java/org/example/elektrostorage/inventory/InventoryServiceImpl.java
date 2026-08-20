package org.example.elektrostorage.inventory;

import org.example.elektrostorage.component.Component;
import org.example.elektrostorage.component.ComponentService;
import org.example.elektrostorage.inventory.dto.CreateInventoryCountDto;
import org.example.elektrostorage.inventory.dto.InventoryCountDto;
import org.example.elektrostorage.inventory.dto.ReceivedComponentDto;
import org.example.elektrostorage.order.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService{
    private final InventoryCountRepository inventoryCountRepository;
    private final ComponentService componentService;
    private final OrderService orderService;

    public InventoryServiceImpl(InventoryCountRepository inventoryCountRepository,
                                ComponentService componentService,
                                OrderService orderService) {
        this.inventoryCountRepository = inventoryCountRepository;
        this.componentService = componentService;
        this.orderService = orderService;
    }

    @Override
    public InventoryCountDto submitCount(CreateInventoryCountDto request){
        Component component = componentService.getComponentById(request.componentId());
        InventoryCount count = new InventoryCount(component, request.countedQuantity(), request.countedBy());
        InventoryCount savedCount = inventoryCountRepository.save(count);
        return toDto(savedCount);
    }

    @Override
    public List<ReceivedComponentDto> getReceivedComponents() {
        return orderService.getReceivedOrders().stream()
                .flatMap(order -> order.getOrderLines().stream()
                        .map(line -> new ReceivedComponentDto(
                                line.getComponent().getName(),
                                line.getQuantity(),
                                order.getReceivedDate()
                        )))
                .toList();
    }


    private InventoryCountDto toDto(InventoryCount savedCount) {
        return new InventoryCountDto(
                savedCount.getId(),
                savedCount.getComponent().getName(),
                savedCount.getCountedQuantity(),
                savedCount.getCountedBy(),
                savedCount.getCountedAt()
        );
    }
}
