package it.unicam.cs.diciotto.progettoids.dto.response;

import it.unicam.cs.diciotto.progettoids.entity.RuoloUtente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RuoloResponse {
    private Long id;
    private RuoloUtente tipoRuolo;
}
