package org.example.elektrostorage.assembly;

import jakarta.persistence.*;
import org.example.elektrostorage.component.Component;

@Entity
public class AssemblyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assembly_list_id", nullable = false)
    private AssemblyList assemblyList;

    @ManyToOne
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    @Column(nullable = false)
    private int quantity;

    protected AssemblyItem() {
    }

    public AssemblyItem(Component component, int quantity) {
        this.component = component;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public AssemblyList getAssemblyList() {
        return assemblyList;
    }

    void setAssemblyList(AssemblyList assemblyList) {
        this.assemblyList = assemblyList;
    }

    public Component getComponent() {
        return component;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}