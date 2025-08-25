package org.lunatic.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ErrorResponseDTO {
    private final int status;
    private final String message;
    @Builder.Default
    private final Instant timestamp = Instant.now();
}