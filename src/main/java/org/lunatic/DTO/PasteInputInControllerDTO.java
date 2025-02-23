package org.lunatic.DTO;

import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class PasteInputInControllerDTO {
    private String text;
    private long liveTime;
    private boolean isPrivate;
}

