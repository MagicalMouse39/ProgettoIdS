package it.unicam.cs.diciotto.progettoids.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Azienda extends BaseEntity {
    private String ragioneSociale;
    private String partitaIva;
    private String descrizioneAzienda;
    private String logo;

    @Enumerated(EnumType.STRING)
    private StatoRegistrazione stato;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "azienda_id")
    private List<Sede> sedi = new ArrayList<>();

    @OneToMany(mappedBy = "produttore", cascade = CascadeType.ALL)
    private List<Prodotto> prodotti = new ArrayList<>();

    @OneToMany(mappedBy = "azienda", cascade = CascadeType.ALL)
    private List<PostSocial> postSocial = new ArrayList<>();
}
