package org.example.elektrostorage.component;

import org.example.elektrostorage.component.dto.*;

import java.util.List;

public interface ComponentService {
    Component getComponentById(Long id);
    List<ComponentDto> getAllComponents();
    ComponentDto createComponent(CreateComponentDto request);
    ComponentDto markAsDiscontinued(Long id);
}