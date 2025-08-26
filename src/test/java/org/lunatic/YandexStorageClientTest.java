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
import org.lunatic.storage.YandexStorageClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YandexStorageClientTest extends SpringBootApplicationTest implements WithAssertions, WithBDDMockito {

  private final YandexStorageClient yandexStorage;
  private final ModelMapper mapper;
  @MockitoBean
  RestTemplate template;

  @Test
  void put_correctUse() {
    PastePutToBlobDTO putDTO = new PastePutToBlobDTO("hash", "яволаимфдлавыоитм");
    HashResponseDTO hashResponseDTO = yandexStorage.put(putDTO);
    PasteResponseDTO pasteResponseDTO = yandexStorage.get("hash");

    assertAll(
      () -> assertThat(hashResponseDTO.getHash()).isEqualTo(putDTO.getHash()),
      () -> assertThat(pasteResponseDTO.getContent()).isEqualTo(putDTO.getText())
    );
  }
}
