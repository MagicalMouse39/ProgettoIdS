package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.entity.PostSocial;

public interface ISocialService {
    PostSocial pubblicaPost(Long aziendaId, Long prodottoId, String testo, String urlImmagine);
}
