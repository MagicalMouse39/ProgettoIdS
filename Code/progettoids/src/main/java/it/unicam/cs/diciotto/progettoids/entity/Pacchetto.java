package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pacchetto extends Prodotto {
    private String contenuto;
}
