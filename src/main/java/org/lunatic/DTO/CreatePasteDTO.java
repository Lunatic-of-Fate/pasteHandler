package org.lunatic.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

// TODO нет @Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
// TODO не понятно что DTO
public class CreatePasteDTO {
    // TODO поля в DTO public final
    private String text;
    private String hash;
    private ZonedDateTime dropDateTime;
    private boolean isPrivate;
}

