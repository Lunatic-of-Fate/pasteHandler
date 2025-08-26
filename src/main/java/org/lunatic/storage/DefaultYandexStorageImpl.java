package org.lunatic.storage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PastePutToBlobDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.exception.PasteNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DefaultYandexStorageImpl implements YandexStorageClient {
    private static final Logger log = LoggerFactory.getLogger(DefaultYandexStorageImpl.class);
    private final String BUCKET_NAME = "melanief";
    private final S3Client s3Client;
    private final ModelMapper mapper;

    @Override
    public HashResponseDTO put(@Valid PastePutToBlobDTO request) {
        byte[] bytes = request.getText().getBytes(StandardCharsets.UTF_8);

        try {
            s3Client.putObject(
              PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(request.getHash())
                .contentType("text/plain; charset=UTF-8")
                .build(),
              RequestBody.fromBytes(bytes)
            );
            log.info("Object uploaded successfully. Hash: {}", request.getHash());

        } catch (S3Exception | SdkClientException e) {
            throw new RuntimeException("Failed to upload object to storage", e);
        }

        return mapper.map(request, HashResponseDTO.class);
    }

    @Override
    public PasteResponseDTO get(String hash) {
        try {
            ResponseInputStream<GetObjectResponse> response =
              s3Client.getObject(
                GetObjectRequest.builder()
                  .bucket(BUCKET_NAME)
                  .key(hash)
                  .build()
              );

            try (var reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8))) {
                var content = reader.lines().collect(Collectors.joining("\n"));
                return PasteResponseDTO.builder()
                  .hash(hash)
                  .content(content.trim())
                  .build();
            }

        } catch (NoSuchKeyException e) {
            throw new PasteNotFoundException("Object not found: " + hash);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new PasteNotFoundException("Object not found: " + hash);
            }
            throw new RuntimeException("S3 error while accessing bucket", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while reading object", e);
        }
    }
}
