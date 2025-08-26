package org.lunatic;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PasteInputInControllerDTO;
import org.lunatic.DTO.PastePutToBlobDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.services.PasteService;
import org.lunatic.storage.YandexStorageClient;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PasteServiceTest extends SpringBootApplicationTest implements WithAssertions, WithBDDMockito {

    private final PasteService pasteService;
    @MockitoBean
    RestTemplate template;
    @MockitoBean
    YandexStorageClient storage;

    @Test
    void put_correctUse() {
        PasteInputInControllerDTO input = new PasteInputInControllerDTO(
                "hello, world!",
                4,
                false
        );
        HashResponseDTO expectedDTO = new HashResponseDTO("hash");
        given(template.getForObject("http://hash-generator:8080/controller/get", HashResponseDTO.class))
                .willReturn(expectedDTO);
        given(storage.put(any())).willReturn(expectedDTO);

        HashResponseDTO result = pasteService.put(input);

        ArgumentCaptor<PastePutToBlobDTO> captor = ArgumentCaptor.forClass(PastePutToBlobDTO.class);
        verify(storage).put(captor.capture());
        PastePutToBlobDTO captureArgument = captor.getValue();

        assertAll(
                () -> assertThat(result.getHash()).isEqualTo("hash"),
                () -> assertThat(captureArgument.getHash()).isEqualTo("hash"),
                () -> assertThat(captureArgument.getText()).isEqualTo("hello, world!")
        );
    }

    @Test
    void search_correctUse() {

        given(storage.get(any())).willReturn(PasteResponseDTO.builder()
                .hash("hash")
                .content("Hello, World!")
                .build()
        );

        PasteResponseDTO response = pasteService.get("hash");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(storage).get(captor.capture());
        String captureArgument = captor.getValue();

        assertAll(
                () -> assertThat(response.getContent()).isEqualTo("Hello, World!"),
                () -> assertThat(captureArgument).isEqualTo("hash")
        );
    }
}
