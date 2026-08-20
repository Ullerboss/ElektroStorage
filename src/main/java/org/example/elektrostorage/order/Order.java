package org.example.elektrostorage.order;

import jakarta.persistence.*;
import org.example.elektrostorage.supplier.Supplier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //order er reserveret af sql
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //liste fremfor enkelt component med antal, til fremtidig udvikling, hvor en leverandør kunne have flere produkter
    //man kunne bestille i én ordre
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    private String trackingCode;

    private LocalDate sentDate;

    private LocalDate expectedDeliveryDate;

    private LocalDate receivedDate;

    protected Order() {
    }

    public Order(Supplier supplier, LocalDate expectedDeliveryDate) {
        this.supplier = supplier;
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public void addOrderLine(OrderLine line) {
        line.setOrder(this);
        orderLines.add(line);
    }

    public boolean isSent() {
        return sentDate != null;
    }

    public boolean isReceived() {
        return receivedDate != null;
    }

    public void markAsSent(LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public void markAsReceived(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Long getId() {
        return id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }
}