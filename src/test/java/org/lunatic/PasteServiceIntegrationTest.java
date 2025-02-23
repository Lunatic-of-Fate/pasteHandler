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
import org.lunatic.storage.YandexStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;

@Nested
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PasteServiceIntegrationTest extends SpringBootApplicationTest implements WithAssertions, WithBDDMockito {

    @Autowired
    PasteService pasteService;
    @MockitoBean
    RestTemplate template;
    @MockitoBean
    YandexStorage storage;
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

        given(storage.put(any())).willReturn(new HashResponseDTO("hash"));

        PasteResponseDTO result = pasteService.put(input);

        assertAll(
                () -> assertThat(result.getText()).isEqualTo("hello, world!")
        );
    }

//    @Test
//    void search_correctUse() {
//        given(hashGeneratorService.generateHash(anyLong())).willReturn("new_hash");
//
//        HashDTO dto = hashService.create();
//        HashDTO searchDto = hashService.search(new HashSearchDTO("new_hash"));
//
//        assertAll(
//                () -> assertThat(searchDto).isNotNull(),
//                () -> assertThat(searchDto.getHash()).isEqualTo(dto.getHash()),
//                () -> assertThat(searchDto.isUsed()).isEqualTo(dto.isUsed())
//        );
//    }
}
