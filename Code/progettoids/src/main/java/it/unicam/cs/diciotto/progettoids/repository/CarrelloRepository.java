package it.unicam.cs.diciotto.progettoids.repository;

import it.unicam.cs.diciotto.progettoids.entity.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
}
