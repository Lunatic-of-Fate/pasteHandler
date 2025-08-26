package org.lunatic.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paste {
    @Id
    @GeneratedValue
    private long Id;
    private String hash;
    private ZonedDateTime createDateTime;
    private ZonedDateTime dropDateTime;
    private boolean isPrivate;
}