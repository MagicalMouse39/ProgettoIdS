package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SedeRequest {
    private String indirizzo;
    private String comune;
    private String cap;
    private double latitudine;
    private double longitudine;
}
