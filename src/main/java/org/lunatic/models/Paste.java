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
public class Paste {
    @Id
    @GeneratedValue
    private long Id;
    private String url;
    @Lob
    private String text;
    private String hash;
    private ZonedDateTime createDateTime;
    private ZonedDateTime dropDateTime;

    private boolean isPrivate;

    public Paste(String url,
                 String text,
                 String hash,
                 ZonedDateTime createDateTime,
                 ZonedDateTime dropDateTime,
                 boolean isPrivate) {
        this.url = url;
        this.text = text;
        this.hash = hash;
        this.createDateTime = createDateTime;
        this.dropDateTime = dropDateTime;
        this.isPrivate = isPrivate;
    }
}