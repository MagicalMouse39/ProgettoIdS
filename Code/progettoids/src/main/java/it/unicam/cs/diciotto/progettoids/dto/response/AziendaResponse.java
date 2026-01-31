package it.unicam.cs.diciotto.progettoids.dto.response;

import it.unicam.cs.diciotto.progettoids.entity.StatoRegistrazione;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AziendaResponse {
    private Long id;
    private String ragioneSociale;
    private String partitaIva;
    private String descrizioneAzienda;
    private String logo;
    private StatoRegistrazione stato;
    private Long utenteId;
    private List<SedeResponse> sedi;
}
