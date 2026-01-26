package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.EventoRequest;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Evento;
import it.unicam.cs.diciotto.progettoids.entity.Sede;
import it.unicam.cs.diciotto.progettoids.repository.AziendaRepository;
import it.unicam.cs.diciotto.progettoids.repository.EventoRepository;
import it.unicam.cs.diciotto.progettoids.repository.SedeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GestoreEventiService {
    private final EventoRepository eventoRepository;
    private final SedeRepository sedeRepository;
    private final AziendaRepository aziendaRepository;

    public GestoreEventiService(
            EventoRepository eventoRepository,
            SedeRepository sedeRepository,
            AziendaRepository aziendaRepository) {
        this.eventoRepository = eventoRepository;
        this.sedeRepository = sedeRepository;
        this.aziendaRepository = aziendaRepository;
    }

    public Evento creaEvento(EventoRequest request) {
        Evento evento = new Evento();
        evento.setTitolo(request.getTitolo());
        evento.setDataInizio(request.getDataInizio());
        evento.setDataFine(request.getDataFine());
        evento.setMaxPartecipanti(request.getMaxPartecipanti());
        evento.setIscrittiAttuali(0);
        if (request.getLuogoId() != null) {
            Sede sede = sedeRepository.findById(request.getLuogoId())
                    .orElseThrow(() -> new IllegalArgumentException("Sede non trovata"));
            evento.setLuogo(sede);
        }
        return eventoRepository.save(evento);
    }

    public Evento modificaEvento(Long eventoId, EventoRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        evento.setTitolo(request.getTitolo());
        evento.setDataInizio(request.getDataInizio());
        evento.setDataFine(request.getDataFine());
        evento.setMaxPartecipanti(request.getMaxPartecipanti());
        if (request.getLuogoId() != null) {
            Sede sede = sedeRepository.findById(request.getLuogoId())
                    .orElseThrow(() -> new IllegalArgumentException("Sede non trovata"));
            evento.setLuogo(sede);
        }
        return eventoRepository.save(evento);
    }

    public Evento invitaEspositore(Long eventoId, Long aziendaId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        evento.getEspositori().add(azienda);
        return eventoRepository.save(evento);
    }

    public Evento prenotaEvento(Long eventoId, Long utenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        if (evento.getIscrittiAttuali() >= evento.getMaxPartecipanti()) {
            throw new IllegalStateException("Evento pieno");
        }
        evento.setIscrittiAttuali(evento.getIscrittiAttuali() + 1);
        return eventoRepository.save(evento);
    }

    public List<Evento> getListaEventi() {
        return eventoRepository.findAll();
    }
}
