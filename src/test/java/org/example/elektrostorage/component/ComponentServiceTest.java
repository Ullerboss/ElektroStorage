package org.example.elektrostorage.component;

import org.example.elektrostorage.component.dto.ComponentDto;
import org.example.elektrostorage.component.dto.CreateComponentDto;
import org.example.elektrostorage.exception.NotFoundException;
import org.example.elektrostorage.supplier.Supplier;
import org.example.elektrostorage.supplier.SupplierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentServiceTest {

    @Mock
    ComponentRepository componentRepository;

    @Mock
    SupplierService supplierService;

    @InjectMocks
    ComponentServiceImpl componentService;

    @Test
    @DisplayName("createComponent - skal kaste NotFoundException hvis leverandøren ikke findes")
    void shouldThrowNotFoundExceptionWhenSupplierDoesNotExist() {
        Long nonExistingSupplierId = 999L;
        CreateComponentDto request = new CreateComponentDto("Freddy", nonExistingSupplierId, "333-222-222");

        when(supplierService.getSupplierById(nonExistingSupplierId))
                .thenThrow(new NotFoundException("Leverandøren med id: " + nonExistingSupplierId + ", blev ikke fundet :/"));

        assertThatThrownBy(() -> componentService.createComponent(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("createComponent - skal gemme og returnere komponenten når leverandøren findes")
    void shouldCreateComponentWhenSupplierExists() {
        Supplier supplier = new Supplier("Elfa", "Adresse 1");
        CreateComponentDto request = new CreateComponentDto("Freddy", 5L, "333-222-222");

        when(supplierService.getSupplierById(5L)).thenReturn(supplier);
        when(componentRepository.save(any(Component.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ComponentDto result = componentService.createComponent(request);

        assertThat(result.name()).isEqualTo("Freddy");
        assertThat(result.supplierName()).isEqualTo("Elfa");
    }
}