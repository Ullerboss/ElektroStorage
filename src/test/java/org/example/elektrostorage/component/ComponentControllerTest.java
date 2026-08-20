package org.example.elektrostorage.component;

import org.example.elektrostorage.component.dto.ComponentDto;
import org.example.elektrostorage.component.dto.CreateComponentDto;
import org.example.elektrostorage.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(ComponentController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComponentControllerTest {

    @Autowired
    MockMvcTester mvc;

    @MockitoBean
    ComponentService service;

    @Test
    @DisplayName("GET /components - skal returnere en liste af komponenter")
    @Order(1)
    void shouldReturnComponentsList() {
        ComponentDto component = new ComponentDto(1L, "LED 5mm rød", "Elfa", "LED-5-RED", false);
        when(service.getAllComponents()).thenReturn(List.of(component));

        var request = mvc.get().uri("/components");

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(org.assertj.core.api.InstanceOfAssertFactories.list(ComponentDto.class))
                .satisfies(components -> {
                    assertThat(components).hasSize(1);
                    assertThat(components.getFirst().name()).isEqualTo("LED 5mm rød");
                });
    }

    @Test
    @DisplayName("POST /components - skal oprette en ny komponent")
    @Order(2)
    void shouldCreateComponent() {
        ComponentDto created = new ComponentDto(1L, "LED 5mm rød", "Elfa", "LED-5-RED", false);
        when(service.createComponent(any(CreateComponentDto.class))).thenReturn(created);

        String requestBody = """
                {
                    "name": "LED 5mm rød",
                    "supplierId": 1,
                    "externalPartNumber": "LED-5-RED"
                }
                """;

        var request = mvc.post()
                .uri("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        assertThat(request)
                .hasStatus(HttpStatus.CREATED)
                .hasHeader(HttpHeaders.LOCATION, "/components/1")
                .bodyJson()
                .convertTo(ComponentDto.class)
                .satisfies(c -> assertThat(c.name()).isEqualTo("LED 5mm rød"));
    }

    @Test
    @DisplayName("PATCH /components/{id}/discontinue - skal markere komponent som udgået")
    @Order(3)
    void shouldMarkComponentAsDiscontinued() {
        Long existingId = 1L;
        ComponentDto discontinued = new ComponentDto(1L, "LED 5mm rød", "Elfa", "LED-5-RED", true);
        when(service.markAsDiscontinued(existingId)).thenReturn(discontinued);

        var request = mvc.patch()
                .uri("/components/{id}/discontinue", existingId)
                .contentType(MediaType.APPLICATION_JSON);

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(ComponentDto.class)
                .satisfies(c -> assertThat(c.discontinued()).isTrue());
    }

    @Test
    @DisplayName("POST /components - skal returnere 404 hvis leverandøren ikke findes")
    @Order(4)
    void shouldReturn404ForNonExistingSupplier() {
        Long nonExistingSupplierId = 999L;
        when(service.createComponent(any(CreateComponentDto.class)))
                .thenThrow(new NotFoundException("Leverandøren med id: " + nonExistingSupplierId + ", blev ikke fundet :/"));

        String requestBody = """
            {
                "name": "Freddy",
                "supplierId": 999,
                "externalPartNumber": "333-222-222"
            }
            """;

        var request = mvc.post()
                .uri("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        assertThat(request)
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .convertTo(ProblemDetail.class);
    }
}