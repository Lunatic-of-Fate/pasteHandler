package org.lunatic.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lunatic.DTO.*;
import org.lunatic.models.Paste;
import org.lunatic.repositories.PasteJpaRepository;
import org.lunatic.storage.YandexStorageClient;
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
    private final YandexStorageClient yandexStorage;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    @Value("${hash.generator.url}")
    private String hashGeneratorUrl;


    @Override
    public HashResponseDTO put(PasteInputInControllerDTO inputDTO) {
        mapper.map(inputDTO, Paste.class);
        Paste paste = Paste.builder()
                .hash(getHashToGenerator().getHash())
                .createDateTime(ZonedDateTime.now())
                .dropDateTime(ZonedDateTime.now().plusHours(inputDTO.getLiveTime()))
                .isPrivate(inputDTO.isPrivate())
                .build();
        pasteJpaRepository.save(paste);

        return mapper.map(
                yandexStorage.put(
                        PastePutToBlobDTO.builder()
                        .hash(paste.getHash())
                        .text(inputDTO.getText())
                        .build()),
                HashResponseDTO.class);
    }

    public PasteResponseDTO get(String hash) {
        return yandexStorage.get(hash);
    }

    private HashResponseDTO getHashToGenerator() {
        return restTemplate.getForObject(hashGeneratorUrl, HashResponseDTO.class);
    }
}
