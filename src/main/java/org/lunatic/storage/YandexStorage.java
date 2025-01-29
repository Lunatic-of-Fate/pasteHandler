package org.lunatic.storage;

import org.lunatic.DTO.CreatePasteDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;

public interface YandexStorage {
    void init();
    void put(CreatePasteDTO request);
    PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO);
}
