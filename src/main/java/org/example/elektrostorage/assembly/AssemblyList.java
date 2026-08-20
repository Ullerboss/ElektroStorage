package org.example.elektrostorage.assembly;

import jakarta.persistence.*;
import org.example.elektrostorage.component.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AssemblyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "result_component_id", nullable = false)
    private Component resultComponent;

    @OneToMany(mappedBy = "assemblyList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssemblyItem> items = new ArrayList<>();

    protected AssemblyList() {
    }

    public AssemblyList(Component resultComponent) {
        this.resultComponent = resultComponent;
    }

    public void addItem(AssemblyItem item) {
        item.setAssemblyList(this);
        items.add(item);
    }

    public Long getId() {
        return id;
    }

    public Component getResultComponent() {
        return resultComponent;
    }

    public List<AssemblyItem> getItems() {
        return items;
    }
}