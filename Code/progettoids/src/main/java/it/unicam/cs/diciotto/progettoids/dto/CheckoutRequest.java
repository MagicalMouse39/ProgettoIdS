package it.unicam.cs.diciotto.progettoids.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private Long carrelloId;
    private String indirizzoConsegna;
    private Long utenteId;
}
