package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pacchetto extends Prodotto {
    @ManyToMany
    @JoinTable(
            name = "pacchetto_tipicita",
            joinColumns = @JoinColumn(name = "pacchetto_id"),
            inverseJoinColumns = @JoinColumn(name = "tipicita_id"))
    private List<Tipicita> tipicita = new ArrayList<>();

    public Pacchetto(String nome, double prezzo, String descrizione) {
        super(nome, prezzo);
        super.setDescrizione(descrizione);
    }
}
