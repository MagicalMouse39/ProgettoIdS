package it.unicam.cs.diciotto.progettoids.dto.response;

import it.unicam.cs.diciotto.progettoids.entity.StatoOrdine;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdineResponse {
    private Long id;
    private String dataCreazione;
    private String indirizzoConsegna;
    private StatoOrdine stato;
    private double totalePagato;
    private String motivazioneRimborso;
    private String noteAmministratore;
    private Long utenteId;
    private List<DettaglioOrdineResponse> dettagli;
}
