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
public class PostSocial extends BaseEntity {
    private String testo;
    private String dataPubblicazione;

    @ManyToOne
    @JoinColumn(name = "azienda_id")
    private Azienda azienda;

    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;
}
