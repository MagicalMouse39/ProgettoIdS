package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RimborsoRequest {
    private Long ordineId;
    private String motivazione;
}
