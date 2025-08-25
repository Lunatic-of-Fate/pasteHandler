package org.lunatic.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PastePutToBlobDTO {
    @NotBlank(message = "Text cannot be null or empty")
    private String hash;
    @NotBlank(message = "Hash cannot be null or empty")
    private String text;
}
