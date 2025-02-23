package org.lunatic.storage;

import org.lunatic.DTO.*;

public interface YandexStorage {
    void init();
    HashResponseDTO put(PastePutToBlobDTO request);
    PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO);
}
