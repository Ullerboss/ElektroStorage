package org.example.elektrostorage.assembly;

import org.example.elektrostorage.assembly.dto.AssemblyItemDto;
import org.example.elektrostorage.assembly.dto.AssemblyListDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(AssemblyController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssemblyControllerTest {

    @Autowired
    MockMvcTester mvc;

    @MockitoBean
    AssemblyService service;

    @Test
    @DisplayName("GET /assemblies - skal returnere en liste af styklister")
    @Order(1)
    void shouldReturnAssemblyListsList() {
        AssemblyListDto assemblyList = new AssemblyListDto(1L, "Lysende LED",
                List.of(new AssemblyItemDto("LED 5mm rød", 1)));
        when(service.getAllAssemblyLists()).thenReturn(List.of(assemblyList));

        var request = mvc.get().uri("/assemblies");

        assertThat(request)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(org.assertj.core.api.InstanceOfAssertFactories.list(AssemblyListDto.class))
                .satisfies(assemblies -> {
                    assertThat(assemblies).hasSize(1);
                    assertThat(assemblies.getFirst().resultComponentName()).isEqualTo("Lysende LED");
                });
    }
}