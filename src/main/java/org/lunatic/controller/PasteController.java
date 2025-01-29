package org.lunatic.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.lunatic.DTO.CreatePasteDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;
import org.lunatic.services.PasteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/controller")
@AllArgsConstructor
@CrossOrigin(origins = "*")

public class PasteController {

    private final PasteService pasteService;
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
    public PasteResponseDTO get(PasteSearchInBlobDTO pasteSearchInBlobDTO) {
        return pasteService.get(pasteSearchInBlobDTO);
    }
    @PutMapping("/put")
    public PasteResponseDTO put(@RequestBody CreatePasteDTO createPasteDTO) {
        pasteService.put(createPasteDTO);
        return new PasteResponseDTO(createPasteDTO.getHash());
    }
}