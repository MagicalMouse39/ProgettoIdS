package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ordine extends BaseEntity {
    private String dataCreazione;
    private String indirizzoConsegna;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato;

    private double totalePagato;
    private String motivazioneRimborso;
    private String noteAmministratore;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ordine_id")
    private List<DettaglioOrdine> dettagli = new ArrayList<>();
}
