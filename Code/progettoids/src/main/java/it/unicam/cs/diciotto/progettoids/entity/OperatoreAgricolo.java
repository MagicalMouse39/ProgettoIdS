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
public class OperatoreAgricolo extends Ruolo {
    @OneToOne
    @JoinColumn(name = "azienda_id")
    private Azienda aziendaGestita;
}
