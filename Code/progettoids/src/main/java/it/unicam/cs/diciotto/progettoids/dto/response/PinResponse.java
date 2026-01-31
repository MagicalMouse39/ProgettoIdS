package it.unicam.cs.diciotto.progettoids.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PinResponse {
    private Long id;
    private double latitudine;
    private double longitudine;
}
