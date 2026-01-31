package it.unicam.cs.diciotto.progettoids.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SedeResponse {
    private Long id;
    private String indirizzo;
    private String comune;
    private String cap;
    private double latitudine;
    private double longitudine;
}
