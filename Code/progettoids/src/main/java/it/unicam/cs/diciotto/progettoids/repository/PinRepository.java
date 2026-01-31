package it.unicam.cs.diciotto.progettoids.repository;

import it.unicam.cs.diciotto.progettoids.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {}
