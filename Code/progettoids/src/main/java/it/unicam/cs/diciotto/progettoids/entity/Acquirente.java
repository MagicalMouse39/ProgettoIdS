package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Acquirente extends Ruolo {
    private String indirizzoSpedizioneDefault;
    private String metodoPagamentoPreferito;

    @OneToOne
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;
}
