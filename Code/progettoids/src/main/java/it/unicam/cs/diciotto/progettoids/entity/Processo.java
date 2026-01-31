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
public class Processo extends Prodotto {
    private String descrizioneFasi;

    @ManyToMany
    @JoinTable(
            name = "processo_ingredienti",
            joinColumns = @JoinColumn(name = "processo_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id"))
    private List<Ingrediente> ingredientiUsati = new ArrayList<>();

    public Processo(String nome, String fasi) {
        setNome(nome);
        this.descrizioneFasi = fasi;
    }
}
