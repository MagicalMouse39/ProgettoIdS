package it.unicam.cs.diciotto.progettoids.repository;

import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Prodotto;
import it.unicam.cs.diciotto.progettoids.entity.StatoProdotto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByStatoCorrente(StatoProdotto statoCorrente);
    List<Prodotto> findByProduttore(Azienda produttore);
    List<Prodotto> findByProduttoreAndStatoCorrente(Azienda produttore, StatoProdotto statoCorrente);
}
