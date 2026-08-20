package org.example.elektrostorage.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryCountRepository extends JpaRepository<InventoryCount, Long> {
}
