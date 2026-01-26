package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sede extends BaseEntity {
    private String indirizzo;
    private String comune;
    private String cap;
    private double latitudine;
    private double longitudine;
}
