package it.unicam.cs.diciotto.progettoids.dto.response;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UtenteResponse {
    private Long id;
    private String email;
    private String nome;
    private String cognome;
    private Set<RuoloResponse> ruoli;
}
