package org.example.elektrostorage.inventory;

import jakarta.persistence.*;
import org.example.elektrostorage.component.Component;

import java.time.LocalDateTime;

@Entity
public class InventoryCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    @Column(nullable = false)
    private int countedQuantity;

    @Column(nullable = false)
    private String countedBy;

    @Column(nullable = false)
    private LocalDateTime countedAt;

    protected InventoryCount() {
    }

    public InventoryCount(Component component, int countedQuantity, String countedBy) {
        this.component = component;
        this.countedQuantity = countedQuantity;
        this.countedBy = countedBy;
        this.countedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Component getComponent() {
        return component;
    }

    public int getCountedQuantity() {
        return countedQuantity;
    }

    public String getCountedBy() {
        return countedBy;
    }

    public LocalDateTime getCountedAt() {
        return countedAt;
    }
}