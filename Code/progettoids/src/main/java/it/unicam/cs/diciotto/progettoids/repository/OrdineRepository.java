package it.unicam.cs.diciotto.progettoids.repository;

import it.unicam.cs.diciotto.progettoids.entity.Ordine;
import it.unicam.cs.diciotto.progettoids.entity.StatoOrdine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByStato(StatoOrdine stato);
}
