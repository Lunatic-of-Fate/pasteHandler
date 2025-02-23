package org.lunatic.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lunatic.DTO.*;
import org.lunatic.models.Paste;
import org.lunatic.repositories.PasteJpaRepository;
import org.lunatic.storage.YandexStorage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultPasteServiceImpl implements PasteService {
    private final PasteJpaRepository pasteJpaRepository;
    private final YandexStorage yandexStorage;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    @Value("${hash.generator.url}")
    private String hashGeneratorUrl;


    @Override
    public PasteResponseDTO put(PasteInputInControllerDTO inputDTO) {
        mapper.map(inputDTO, Paste.class);

        Paste paste = Paste.builder()
                .text(inputDTO.getText())
                .hash(getHashToGenerator().getHash())
                .createDateTime(ZonedDateTime.now())
                .dropDateTime(ZonedDateTime.now().plusHours(inputDTO.getLiveTime()))
                .isPrivate(false)
                .build();
        pasteJpaRepository.save(paste);
        return mapper.map(
                yandexStorage.put(mapper.map(paste, PastePutToBlobDTO.class)), PasteResponseDTO.class);
    }

    public PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO) {
        return yandexStorage.get(pasteSearchInBlobDTO);
    }

    private HashResponseDTO getHashToGenerator() {
        return restTemplate.getForObject(hashGeneratorUrl, HashResponseDTO.class);
    }
}
