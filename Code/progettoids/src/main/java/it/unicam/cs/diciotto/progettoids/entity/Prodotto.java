package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Prodotto extends BaseEntity {
    private String nome;
    private String descrizione;
    private String provenienza;
    private String immagine;
    private double prezzo;
    private int quantitaDisponibile;
    private String dataScadenza;

    @Enumerated(EnumType.STRING)
    private StatoProdotto statoCorrente;

    @ManyToOne
    @JoinColumn(name = "azienda_id")
    private Azienda produttore;
}
