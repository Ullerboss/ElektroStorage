package org.example.elektrostorage.supplier;

import org.example.elektrostorage.exception.NotFoundException;
import org.example.elektrostorage.supplier.dto.SupplierDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Leverandør med id " + id + " findes ikke"));
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private SupplierDto toDto(Supplier supplier) {
        return new SupplierDto(supplier.getId(), supplier.getName());
    }
}