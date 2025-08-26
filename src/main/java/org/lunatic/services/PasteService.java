package org.lunatic.services;

import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PasteInputInControllerDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;

public interface PasteService {
    HashResponseDTO put(PasteInputInControllerDTO request);
    PasteResponseDTO get(String hash);
}
