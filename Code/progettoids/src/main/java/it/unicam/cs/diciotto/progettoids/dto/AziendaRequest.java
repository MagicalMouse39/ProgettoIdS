package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AziendaRequest {
    private String ragioneSociale;
    private String partitaIva;
    private String descrizioneAzienda;
    private String logo;
    private List<SedeRequest> sedi;
    private Long utenteId;
}
