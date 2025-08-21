package org.lunatic.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PastePutToBlobDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class DefaultYandexStorageImpl implements YandexStorage {
    private static final Logger log = LoggerFactory.getLogger(DefaultYandexStorageImpl.class);
    private final String BUCKET_NAME = "melanief";
    private final AmazonS3 s3Config;
    private final ModelMapper mapper;

    public HashResponseDTO put(PastePutToBlobDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getText() == null || request.getText().isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        if (request.getHash() == null || request.getHash().isBlank()) {
            throw new IllegalArgumentException("Hash cannot be null or empty");
        }

        try (InputStream inputStream = new ByteArrayInputStream(
                request.getText().getBytes(StandardCharsets.UTF_8))
        ) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(request.getText().getBytes().length);
            metadata.setContentType("text/plain; charset=UTF-8");

            s3Config.putObject(BUCKET_NAME, request.getHash(), inputStream, metadata);
            log.info("Object uploaded successfully. Hash: {}", request.getHash());

        } catch (SdkClientException e) {
            log.error("S3 upload failed. Hash: {}, Error: {}", request.getHash(), e.getMessage());
            throw new RuntimeException("Failed to upload object to storage", e);
        } catch (Exception e) {
            log.error("Unexpected error. Hash: {}", request.getHash(), e);
            throw new RuntimeException("Internal server error", e);
        }
        return mapper.map(request, HashResponseDTO.class);
    }

    public PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO) {
        StringBuilder result = new StringBuilder();
        try {
            S3Object s3Object = s3Config.getObject(BUCKET_NAME, pasteSearchInBlobDTO.getHash());
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n"); // Добавляем строку и перевод строки
                }
            } catch (Exception e) {
                System.err.println("Error reading from S3: " + e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    System.err.println("Error closing input stream: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving object from S3: " + e.getMessage());
        }
        return new PasteResponseDTO(result.toString().trim());
    }
}