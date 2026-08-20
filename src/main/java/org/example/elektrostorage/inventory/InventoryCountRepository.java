package org.example.elektrostorage.inventory;

import org.example.elektrostorage.component.Component;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryCountRepository extends JpaRepository<InventoryCount, Long> {
    Optional<InventoryCount> findTopByComponentOrderByCountedAtDesc(Component component);
}