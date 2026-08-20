package org.example.elektrostorage.inventory;

import org.example.elektrostorage.inventory.dto.CreateInventoryCountDto;
import org.example.elektrostorage.inventory.dto.InventoryCountDto;
import org.example.elektrostorage.inventory.dto.ReceivedComponentDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(InventoryController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryControllerTest {

    @Autowired
    MockMvcTester mvc;

    @MockitoBean
    InventoryService service;

    @Test
    @DisplayName("GET /inventory - skal returnere en liste af modtagne komponenter")
    @Order(1)
    void shouldReturnReceivedComponentsList() {
        ReceivedComponentDto received = new ReceivedComponentDto("LED 5mm rød", 100, LocalDate.parse("2026-03-01"));
        when(service.getReceivedComponents()).thenReturn(List.of(received));

        var request = mvc.get().uri("/inventory");

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(org.assertj.core.api.InstanceOfAssertFactories.list(ReceivedComponentDto.class))
                .satisfies(components -> {
                    assertThat(components).hasSize(1);
                    assertThat(components.getFirst().componentName()).isEqualTo("LED 5mm rød");
                });
    }

    @Test
    @DisplayName("POST /inventory/counts - skal indsende en optælling")
    @Order(2)
    void shouldSubmitCount() {
        InventoryCountDto created = new InventoryCountDto(1L, "LED 5mm rød", 95, "Frederik", LocalDateTime.parse("2026-03-01T10:00:00"));
        when(service.submitCount(any(CreateInventoryCountDto.class))).thenReturn(created);

        String requestBody = """
                {
                    "componentId": 1,
                    "countedQuantity": 95,
                    "countedBy": "Frederik"
                }
                """;

        var request = mvc.post()
                .uri("/inventory/counts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        assertThat(request)
                .hasStatus(HttpStatus.CREATED)
                .hasHeader(HttpHeaders.LOCATION, "/inventory/counts/1")
                .bodyJson()
                .convertTo(InventoryCountDto.class)
                .satisfies(c -> assertThat(c.countedQuantity()).isEqualTo(95));
    }
}