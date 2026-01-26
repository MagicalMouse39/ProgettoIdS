package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DettaglioOrdine extends BaseEntity {
    private int quantita;
    private double prezzoUnitarioSnapshot;

    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodottoRiferimento;
}
