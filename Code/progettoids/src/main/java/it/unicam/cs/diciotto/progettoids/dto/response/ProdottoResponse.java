package it.unicam.cs.diciotto.progettoids.dto.response;

import it.unicam.cs.diciotto.progettoids.entity.StatoProdotto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProdottoResponse {
    private Long id;
    private String tipo;
    private String nome;
    private String descrizione;
    private String provenienza;
    private String immagine;
    private double prezzo;
    private int quantitaDisponibile;
    private String dataScadenza;
    private StatoProdotto statoCorrente;
    private Long produttoreId;
    private String certificazioni;
    private String descrizioneFasi;
    private Long processoId;
    private java.util.List<Long> tipicitaIds;
    private java.util.List<Long> ingredientiIds;
}
