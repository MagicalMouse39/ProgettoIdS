package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessoRequest {
    private String nome;
    private String descrizione;
    private String provenienza;
    private String immagine;
    private double prezzo;
    private int quantitaDisponibile;
    private String dataScadenza;
    private String descrizioneFasi;
    private java.util.List<Long> ingredientiIds;
    private Long produttoreId;
}
