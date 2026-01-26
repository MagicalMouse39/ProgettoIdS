package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AziendaRequest {
    private String ragioneSociale;
    private String partitaIva;
    private String descrizioneAzienda;
    private String logo;
}
