package org.example.elektrostorage.supplier;

import org.example.elektrostorage.supplier.dto.SupplierDto;

import java.util.List;

public interface SupplierService {
    Supplier getSupplierById(Long id);
    List<SupplierDto> getAllSuppliers();
}