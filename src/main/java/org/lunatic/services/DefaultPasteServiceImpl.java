package org.lunatic.services;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lunatic.DTO.CreatePasteDTO;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;
import org.lunatic.models.Paste;
import org.lunatic.repositories.PasteJpaRepository;
import org.lunatic.storage.YandexStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Service
@Slf4j
public class DefaultPasteServiceImpl implements PasteService {
    private final PasteJpaRepository pasteJpaRepository;
    private final YandexStorage yandexStorage;
    private final RestTemplate restTemplate;


    @Override
    public PasteResponseDTO put(CreatePasteDTO request) {
        // TODO изменить на mapper
        String hash = getHashToGenerator().getHash();
        request.setHash(hash);
        log.info(hash);
        Paste paste = new Paste(
                putToBlob(request),
                request.getText(),
                request.getHash(),
                ZonedDateTime.now(),
                request.getDropDateTime(),
                request.isPrivate()
        );
        pasteJpaRepository.save(paste);
        return new PasteResponseDTO(hash);
    }

    public PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO) {
        return yandexStorage.get(pasteSearchInBlobDTO);
    }

    private String putToBlob(CreatePasteDTO createPasteDTO) {
        yandexStorage.put(createPasteDTO);
        return createPasteDTO.getHash();
    }
    private HashResponseDTO getHashToGenerator() {
        String url = "http://hash-generator:8080/controller/get";
        return restTemplate.getForObject(url, HashResponseDTO.class);
    }
}
