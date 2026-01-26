package it.unicam.cs.diciotto.progettoids.repository;

import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.StatoRegistrazione;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AziendaRepository extends JpaRepository<Azienda, Long> {
    List<Azienda> findByStato(StatoRegistrazione stato);
}
