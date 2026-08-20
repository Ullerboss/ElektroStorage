package org.example.elektrostorage.component;

import org.example.elektrostorage.component.dto.ComponentDto;
import org.example.elektrostorage.component.dto.CreateComponentDto;
import org.example.elektrostorage.exception.NotFoundException;
import org.example.elektrostorage.supplier.Supplier;
import org.example.elektrostorage.supplier.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;
    private final SupplierService supplierService;

    ComponentServiceImpl(ComponentRepository componentRepository, SupplierService supplierService) {
        this.componentRepository = componentRepository;
        this.supplierService = supplierService;
    }

    @Override
    public Component getComponentById(Long id){
        return componentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Komponenten med id: "+id+", blev ikke fundet :/"));
    }

    @Override
    public List<ComponentDto> getAllComponents() {
        return componentRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ComponentDto createComponent(CreateComponentDto request) {
        Supplier supplier = supplierService.getSupplierById(request.supplierId());
        Component component = new Component(request.name(), supplier, request.externalPartNumber());
        Component saved = componentRepository.save(component);
        return toDto(saved);
    }

    @Override
    public ComponentDto markAsDiscontinued(Long id){
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Komponenten med id: "+id+", blev ikke fundet :/"));
        component.setDiscontinued(true);
        Component savedComponent = componentRepository.save(component);
        return toDto(savedComponent);
    }


    private ComponentDto toDto(Component component) {
        return new ComponentDto(
                component.getId(),
                component.getName(),
                component.getSupplier().getName(),
                component.getExternalPartNumber(),
                component.isDiscontinued()
        );
    }
}