package org.example.elektrostorage.inventory;

import org.example.elektrostorage.component.Component;
import org.example.elektrostorage.component.ComponentService;
import org.example.elektrostorage.exception.BadRequestException;
import org.example.elektrostorage.inventory.dto.CreateInventoryCountDto;
import org.example.elektrostorage.inventory.dto.InventoryCountDto;
import org.example.elektrostorage.inventory.dto.ReceivedComponentDto;
import org.example.elektrostorage.order.OrderLine;
import org.example.elektrostorage.order.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public InventoryCountDto submitCount(CreateInventoryCountDto request) {
        Component component = componentService.getComponentById(request.componentId());

        boolean isOnStock = orderService.getReceivedOrders().stream()
                .flatMap(order -> order.getOrderLines().stream())
                .anyMatch(line -> line.getComponent().getId().equals(component.getId()));

        if (!isOnStock) {
            throw new BadRequestException("Denne komponent kan ikke optælles, da den ikke er på lager");
        }

        InventoryCount count = new InventoryCount(component, request.countedQuantity(), request.countedBy());
        InventoryCount savedCount = inventoryCountRepository.save(count);
        return toDto(savedCount);
    }

    @Override
    public List<ReceivedComponentDto> getReceivedComponents() {
        return orderService.getReceivedOrders().stream()
                .flatMap(order -> order.getOrderLines().stream()
                        .map(line -> toReceivedComponentDto(line, order.getReceivedDate())))
                .toList();
    }

    private ReceivedComponentDto toReceivedComponentDto(OrderLine line, LocalDate receivedDate) {
        Component component = line.getComponent();
        Optional<InventoryCount> latestCount = inventoryCountRepository.findTopByComponentOrderByCountedAtDesc(component);

        boolean countedSinceReceived = latestCount.isPresent()
                && !latestCount.get().getCountedAt().toLocalDate().isBefore(receivedDate);

        if (countedSinceReceived) {
            InventoryCount count = latestCount.get();
            return new ReceivedComponentDto(
                    component.getId(),
                    component.getName(),
                    count.getCountedQuantity(),
                    count.getCountedAt().toLocalDate(),
                    count.getCountedBy()
            );
        }

        return new ReceivedComponentDto(component.getId(), component.getName(), line.getQuantity(), receivedDate, null);
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
