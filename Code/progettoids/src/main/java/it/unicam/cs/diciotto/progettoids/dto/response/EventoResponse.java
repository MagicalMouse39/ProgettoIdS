package it.unicam.cs.diciotto.progettoids.dto.response;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventoResponse {
    private Long id;
    private String titolo;
    private String dataInizio;
    private String dataFine;
    private int maxPartecipanti;
    private int iscrittiAttuali;
    private Long luogoId;
    private Set<Long> espositoriIds;
    private Set<Long> partecipantiIds;
}
