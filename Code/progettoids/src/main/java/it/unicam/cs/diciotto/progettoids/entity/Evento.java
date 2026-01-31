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

    public Evento(String titolo, String dataInizio) {
        this.titolo = titolo;
        this.dataInizio = dataInizio;
    }

    public void addEspositore(Azienda azienda) {
        if (azienda != null) {
            espositori.add(azienda);
        }
    }

    public void addPartecipante(Utente utente) {
        if (utente != null) {
            partecipanti.add(utente);
        }
    }

    public boolean isPieno() {
        return iscrittiAttuali >= maxPartecipanti;
    }
}
