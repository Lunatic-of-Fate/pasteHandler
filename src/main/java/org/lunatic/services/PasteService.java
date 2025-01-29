package org.lunatic.services;

import org.lunatic.DTO.CreatePasteDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;

public interface PasteService {
    PasteResponseDTO put(CreatePasteDTO request);
    PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO);
}
