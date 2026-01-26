package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.PostSocial;
import it.unicam.cs.diciotto.progettoids.entity.Prodotto;
import it.unicam.cs.diciotto.progettoids.repository.AziendaRepository;
import it.unicam.cs.diciotto.progettoids.repository.PostSocialRepository;
import it.unicam.cs.diciotto.progettoids.repository.ProdottoRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class SocialServiceImpl implements ISocialService {
    private final PostSocialRepository postSocialRepository;
    private final AziendaRepository aziendaRepository;
    private final ProdottoRepository prodottoRepository;

    public SocialServiceImpl(
            PostSocialRepository postSocialRepository,
            AziendaRepository aziendaRepository,
            ProdottoRepository prodottoRepository) {
        this.postSocialRepository = postSocialRepository;
        this.aziendaRepository = aziendaRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @Override
    public PostSocial pubblicaPost(Long aziendaId, Long prodottoId, String testo) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        PostSocial post = new PostSocial();
        post.setAzienda(azienda);
        post.setProdotto(prodotto);
        post.setTesto(testo);
        post.setDataPubblicazione(LocalDate.now().toString());
        return postSocialRepository.save(post);
    }
}
