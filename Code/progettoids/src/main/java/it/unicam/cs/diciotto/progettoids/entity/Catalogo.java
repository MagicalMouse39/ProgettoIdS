package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Catalogo extends BaseEntity {
    @Transient
    private Catalogo _instance;
    public Catalogo getInstance() {
        if (_instance == null) {
            _instance = new Catalogo();
        }
        return _instance;
    }

    private String dataUltimoAggiornamento;
    private double sogliaSpedizioneGratuita;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "catalogo_id")
    private List<Prodotto> prodotti = new ArrayList<>();

    public void addProdotto(Prodotto prodotto) {
        if (prodotto != null && !prodotti.contains(prodotto)) {
            prodotti.add(prodotto);
        }
    }

    public List<Prodotto> cercaPerStato(StatoProdotto stato) {
        if (stato == null) {
            return new ArrayList<>();
        }
        return prodotti.stream()
                .filter(p -> p != null && stato.equals(p.getStatoCorrente()))
                .collect(Collectors.toList());
    }

    public List<Prodotto> cercaPerAzienda(Azienda azienda) {
        if (azienda == null) {
            return new ArrayList<>();
        }
        return prodotti.stream()
                .filter(p -> p != null && azienda.equals(p.getProduttore()))
                .collect(Collectors.toList());
    }
}
