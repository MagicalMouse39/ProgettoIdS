package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ordine_id")
    private List<DettaglioOrdine> dettagli = new ArrayList<>();

    public Ordine(Carrello carrello) {
        if (carrello == null || carrello.getProdotti() == null) {
            return;
        }
        Map<Long, DettaglioOrdine> aggregati = new LinkedHashMap<>();
        for (Map.Entry<Prodotto, Integer> entry : carrello.getProdotti().entrySet()) {
            Prodotto prodotto = entry.getKey();
            Integer quantita = entry.getValue();
            if (prodotto == null || prodotto.getId() == null || quantita == null || quantita <= 0) {
                continue;
            }
            DettaglioOrdine dettaglio = new DettaglioOrdine();
            dettaglio.setProdottoRiferimento(prodotto);
            dettaglio.setQuantita(quantita);
            dettaglio.setPrezzoUnitarioSnapshot(prodotto.getPrezzo());
            aggregati.put(prodotto.getId(), dettaglio);
        }
        this.dettagli = new ArrayList<>(aggregati.values());
        this.totalePagato = this.dettagli.stream()
                .mapToDouble(d -> d.getPrezzoUnitarioSnapshot() * d.getQuantita())
                .sum();
    }
}
