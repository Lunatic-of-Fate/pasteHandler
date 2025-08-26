package org.lunatic.controller;

import lombok.AllArgsConstructor;
import org.lunatic.DTO.HashResponseDTO;
import org.lunatic.DTO.PasteInputInControllerDTO;
import org.lunatic.DTO.PasteResponseDTO;
import org.lunatic.DTO.PasteSearchInBlobDTO;
import org.lunatic.services.PasteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pastes")
@AllArgsConstructor
@CrossOrigin(origins = "*")

public class PasteController {

    private final PasteService pasteService;

    @GetMapping(value = "/{hash}")
    public PasteResponseDTO get(@PathVariable String hash) {
        return pasteService.get(hash);
    }

    @PutMapping("/put")
    public HashResponseDTO put(@RequestBody PasteInputInControllerDTO pasteInputInControllerDTO) {
        return pasteService.put(pasteInputInControllerDTO);
    }
}