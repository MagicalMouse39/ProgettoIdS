package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Evento extends BaseEntity {
    private String titolo;
    private String dataInizio;
    private String dataFine;
    private int maxPartecipanti;
    private int iscrittiAttuali;

    @ManyToOne
    @JoinColumn(name = "sede_id")
    private Sede luogo;

    @ManyToMany
    private Set<Azienda> espositori = new HashSet<>();

    @ManyToMany
    private Set<Utente> partecipanti = new HashSet<>();
}
