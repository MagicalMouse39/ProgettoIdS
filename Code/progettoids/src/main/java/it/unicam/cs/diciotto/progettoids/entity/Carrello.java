package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Carrello extends BaseEntity {
    private String dataCreazione;

    @ElementCollection
    @CollectionTable(name = "carrello_prodotti", joinColumns = @JoinColumn(name = "carrello_id"))
    @MapKeyJoinColumn(name = "prodotto_id")
    @Column(name = "quantita")
    private Map<Prodotto, Integer> prodotti = new HashMap<>();

    public double getTotale() {
        return this.prodotti.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .mapToDouble(entry -> entry.getKey().getPrezzo() * entry.getValue())
                .sum();
    }

    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        if (prodotto == null) {
            return;
        }
        int qty = Math.max(1, quantita);
        prodotti.merge(prodotto, qty, Integer::sum);
    }

    public void svuota() {
        prodotti.clear();
    }
}
