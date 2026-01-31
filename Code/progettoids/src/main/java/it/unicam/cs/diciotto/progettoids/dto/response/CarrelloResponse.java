package it.unicam.cs.diciotto.progettoids.dto.response;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarrelloResponse {
    private Long id;
    private String dataCreazione;
    private Map<Long, Integer> prodottoQuantita;
    private double totale;
}
