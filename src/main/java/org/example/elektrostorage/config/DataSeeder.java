package org.example.elektrostorage.config;

import org.example.elektrostorage.assembly.AssemblyItem;
import org.example.elektrostorage.assembly.AssemblyList;
import org.example.elektrostorage.assembly.AssemblyListRepository;
import org.example.elektrostorage.component.Component;
import org.example.elektrostorage.component.ComponentRepository;
import org.example.elektrostorage.order.Order;
import org.example.elektrostorage.order.OrderLine;
import org.example.elektrostorage.order.OrderRepository;
import org.example.elektrostorage.supplier.Supplier;
import org.example.elektrostorage.supplier.SupplierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;


@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            SupplierRepository supplierRepository,
            ComponentRepository componentRepository,
            OrderRepository orderRepository,
            AssemblyListRepository assemblyListRepository
    ) {
        return args -> {

            // Suppliers
            Supplier supplierA = supplierRepository.save(new Supplier("NordComp A/S", "Fabriksvej 3, 2600 Glostrup"));
            Supplier supplierB = supplierRepository.save(new Supplier("ElektroPartner ApS", "Industriparken 10, 2750 Ballerup"));
            Supplier supplierC = supplierRepository.save(new Supplier("Baltic Components", "Rīgas iela 5, Riga, Letland"));

            // Components
            Component led = componentRepository.save(new Component("LED 5 mm, rød", supplierA, "LED-5MM-RED"));
            Component resistor1k = componentRepository.save(new Component("Modstand, 1 kOhm", supplierA, "RES-1K-0207"));
            Component batteryHolder = componentRepository.save(new Component("Batteriholder til 9V batteri", supplierB, "BH-9V-STD"));
            Component battery9v = componentRepository.save(new Component("9V batteri", supplierB, "BAT-9V-ALK"));
            Component capacitor = componentRepository.save(new Component("Kondensator, 100 µF 16V", supplierA, "CAP-100UF-16V"));
            Component resistor4k7 = componentRepository.save(new Component("Modstand, 4,7 kOhm", supplierA, "RES-4K7-0207"));
            Component switchComp = componentRepository.save(new Component("Trykknap, 6 mm", supplierB, "SW-SPST-6MM"));
            Component wire = componentRepository.save(new Component("Ledningstråd, rød, 1 m", supplierC, "WIRE-RED-1M"));
            Component pcb = componentRepository.save(new Component("Prototypeprint, 50x70 mm", supplierC, "PCB-50X70-STD"));
            Component fuse = componentRepository.save(new Component("Sikring, 1A 5x20 mm", supplierC, "FUSE-1A-5X20"));

            // Active order (ingen sentDate endnu)
            Order draftOrder = new Order(supplierA, LocalDate.now().plusDays(10));
            draftOrder.addOrderLine(new OrderLine(led, 20));
            orderRepository.save(draftOrder);

            // Sent order, not recieved
            Order sentOrder = new Order(supplierB, LocalDate.now().plusDays(5));
            sentOrder.addOrderLine(new OrderLine(batteryHolder, 10));
            sentOrder.addOrderLine(new OrderLine(battery9v, 10));
            sentOrder.addOrderLine(new OrderLine(switchComp, 10));
            sentOrder.setTrackingCode("TRACK-12345");
            sentOrder.markAsSent(LocalDate.now().minusDays(2));
            orderRepository.save(sentOrder);

            // Complete order
            Order completedOrder = new Order(supplierC, LocalDate.now().minusDays(10));
            completedOrder.addOrderLine(new OrderLine(resistor1k, 100));
            completedOrder.setTrackingCode("TRACK-98765");
            completedOrder.markAsSent(LocalDate.now().minusDays(10));
            completedOrder.markAsReceived(LocalDate.now().minusDays(3));
            orderRepository.save(completedOrder);

            // Result Component
            Component lysendeLed = componentRepository.save(new Component("Lysende LED", supplierA, "KIT-LYSENDE-LED"));
            AssemblyList lysendeLedKit = new AssemblyList(lysendeLed);
            lysendeLedKit.addItem(new AssemblyItem(led, 1));
            lysendeLedKit.addItem(new AssemblyItem(resistor1k, 1));
            lysendeLedKit.addItem(new AssemblyItem(batteryHolder, 1));
            lysendeLedKit.addItem(new AssemblyItem(battery9v, 1));
            assemblyListRepository.save(lysendeLedKit);
        };
    }
}