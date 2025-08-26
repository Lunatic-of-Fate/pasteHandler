package org.lunatic.storage;

import org.lunatic.DTO.*;

public interface YandexStorageClient {
    HashResponseDTO put(PastePutToBlobDTO request);
    PasteResponseDTO get(String hash);
}
