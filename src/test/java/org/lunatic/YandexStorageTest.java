package org.lunatic;


import info.solidsoft.mockito.java8.api.WithBDDMockito;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PastePutToBlobDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;
import org.lunatic.storage.YandexStorage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;

@Nested
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YandexStorageTest extends SpringBootApplicationTest implements WithAssertions, WithBDDMockito {

    private final YandexStorage yandexStorage;
    private final ModelMapper mapper;
    @MockitoBean
    RestTemplate template;

    @Test
    void put_correctUse() {
        PastePutToBlobDTO putDTO = new PastePutToBlobDTO("hash", "яволаимфдлавыоитм");
        PasteSearchInBlobDTO searchDTO = mapper.map(putDTO, PasteSearchInBlobDTO.class);

        HashResponseDTO hashResponseDTO = yandexStorage.put(putDTO);
        PasteResponseDTO pasteResponseDTO = yandexStorage.get(searchDTO);

        assertAll(
                () -> assertThat(hashResponseDTO.getHash()).isEqualTo(putDTO.getHash()),
                () -> assertThat(pasteResponseDTO.getText()).isEqualTo(putDTO.getText())
        );
    }
    @Test
    void get_correctUse() {

    }
}
