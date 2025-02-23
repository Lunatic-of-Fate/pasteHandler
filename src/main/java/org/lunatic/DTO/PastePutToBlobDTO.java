package org.lunatic.DTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PastePutToBlobDTO {
    private String hash;
    private String text;

}
