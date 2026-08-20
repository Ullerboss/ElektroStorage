package org.example.elektrostorage.component;

import jakarta.persistence.*;
import org.example.elektrostorage.supplier.Supplier;

@Entity
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private String externalPartNumber;

    @Column(nullable = false)
    private boolean discontinued = false;

    protected Component() {
    }

    public Component(String name, Supplier supplier, String externalPartNumber) {
        this.name = name;
        this.supplier = supplier;
        this.externalPartNumber = externalPartNumber;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    public String getExternalPartNumber() {
        return externalPartNumber;
    }
    public void setExternalPartNumber(String externalPartNumber) {
        this.externalPartNumber = externalPartNumber;
    }
    public boolean isDiscontinued() {
        return discontinued;
    }
    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }
}