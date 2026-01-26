package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventoRequest {
    private String titolo;
    private String dataInizio;
    private String dataFine;
    private int maxPartecipanti;
    private Long luogoId;
}
