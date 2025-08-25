package org.lunatic.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PastePutToBlobDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;
import org.lunatic.exception.PasteNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DefaultYandexStorageImpl implements YandexStorage {
    private static final Logger log = LoggerFactory.getLogger(DefaultYandexStorageImpl.class);
    private final String BUCKET_NAME = "melanief";
    private final AmazonS3 s3Config;
    private final ModelMapper mapper;

    public HashResponseDTO put(@Valid PastePutToBlobDTO request) {
        byte[] bytes = request.getText().getBytes(StandardCharsets.UTF_8);

        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            metadata.setContentType("text/plain; charset=UTF-8");

            s3Config.putObject(BUCKET_NAME, request.getHash(), inputStream, metadata);
            log.info("Object uploaded successfully. Hash: {}", request.getHash());

        } catch (SdkClientException | IOException e) {
            throw new RuntimeException("Failed to upload object to storage", e);
        }
        return mapper.map(request, HashResponseDTO.class);
    }

    public PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO) {
        try (var s3Object = s3Config.getObject(BUCKET_NAME, pasteSearchInBlobDTO.getHash());
             var inputStream = s3Object.getObjectContent();
             var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            var content = reader.lines().collect(Collectors.joining("\n"));
            return PasteResponseDTO.builder()
                    .hash(pasteSearchInBlobDTO.getHash())
                    .content(content.trim())
                    .build();
        } catch (AmazonS3Exception e) {
            if ("NoSuchKey".equals(e.getErrorCode()) || e.getStatusCode() == 404) {
                throw new PasteNotFoundException("Object not found: " + pasteSearchInBlobDTO.getHash());
            }
            throw new RuntimeException("S3 error while accessing bucket", e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}