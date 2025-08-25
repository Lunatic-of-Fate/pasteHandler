package org.lunatic.common.controller;

import org.lunatic.DTO.ErrorResponseDTO;
import org.lunatic.exception.PasteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HttpErrorHandler {
    @ExceptionHandler(PasteNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> notFound(PasteNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO
                        .builder()
                        .status(404)
                        .message(e.getMessage())
                        .build());
    }
}
