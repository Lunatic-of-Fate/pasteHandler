package org.lunatic.services;

import org.lunatic.DTO.PasteInputInControllerDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;

public interface PasteService {
    PasteResponseDTO put(PasteInputInControllerDTO request);
    PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO);
}
