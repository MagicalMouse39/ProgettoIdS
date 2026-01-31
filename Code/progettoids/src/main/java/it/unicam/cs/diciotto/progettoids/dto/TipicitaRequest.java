package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipicitaRequest {
    private String nome;
    private String descrizione;
    private String provenienza;
    private String immagine;
    private double prezzo;
    private int quantitaDisponibile;
    private String dataScadenza;
    private String certificazioni;
    private Long processoId;
    private Long produttoreId;
}
