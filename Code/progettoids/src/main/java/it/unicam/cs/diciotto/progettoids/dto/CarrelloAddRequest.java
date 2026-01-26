package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarrelloAddRequest {
    private Long carrelloId;
    private Long prodottoId;
    private int quantita;
}
