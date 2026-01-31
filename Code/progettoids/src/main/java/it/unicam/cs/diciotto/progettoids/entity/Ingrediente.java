package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ingrediente extends Prodotto {
    private String certificazioni;

    public Ingrediente(String nome, String certificazione) {
        setNome(nome);
        this.certificazioni = certificazione;
    }
}
