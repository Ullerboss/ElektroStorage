package org.example.elektrostorage.supplier;

import org.example.elektrostorage.exception.NotFoundException;
import org.springframework.stereotype.Service;

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
}