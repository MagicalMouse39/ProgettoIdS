package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.EventoRequest;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Evento;
import it.unicam.cs.diciotto.progettoids.entity.Sede;
import it.unicam.cs.diciotto.progettoids.entity.StatoRegistrazione;
import it.unicam.cs.diciotto.progettoids.repository.AziendaRepository;
import it.unicam.cs.diciotto.progettoids.repository.EventoRepository;
import it.unicam.cs.diciotto.progettoids.repository.SedeRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GestoreEventiService {
    private final EventoRepository eventoRepository;
    private final SedeRepository sedeRepository;
    private final AziendaRepository aziendaRepository;
    private final INotificaService notificaService;

    public GestoreEventiService(
            EventoRepository eventoRepository,
            SedeRepository sedeRepository,
            AziendaRepository aziendaRepository,
            INotificaService notificaService) {
        this.eventoRepository = eventoRepository;
        this.sedeRepository = sedeRepository;
        this.aziendaRepository = aziendaRepository;
        this.notificaService = notificaService;
    }

    public Evento creaEvento(EventoRequest request) {
        Evento evento = new Evento(request.getTitolo(), request.getDataInizio());
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
        if (eventoRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("Nessun evento disponibile");
        }
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        if (!azienda.getStato().equals(StatoRegistrazione.APPROVATA)) {
            throw new IllegalArgumentException("Azienda non approvata");
        }
        evento.addEspositore(azienda);
        Evento saved = eventoRepository.save(evento);
        notificaInvitoEspositore(saved, azienda);
        return saved;
    }

    public Evento prenotaEvento(Long eventoId, Long utenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        verificaEventoNonPassato(evento);
        if (evento.getIscrittiAttuali() >= evento.getMaxPartecipanti()) {
            throw new IllegalStateException("Evento pieno");
        }
        evento.setIscrittiAttuali(evento.getIscrittiAttuali() + 1);
        return eventoRepository.save(evento);
    }

    public List<Evento> getListaEventi() {
        return eventoRepository.findAll();
    }

    private void verificaEventoNonPassato(Evento evento) {
        String dataRiferimento = evento.getDataFine() != null && !evento.getDataFine().isBlank()
                ? evento.getDataFine()
                : evento.getDataInizio();
        if (dataRiferimento == null || dataRiferimento.isBlank()) {
            return;
        }
        LocalDate dataEvento;
        try {
            dataEvento = LocalDate.parse(dataRiferimento);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Data evento non valida");
        }
        if (dataEvento.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Evento già passato");
        }
    }

    private void notificaInvitoEspositore(Evento evento, Azienda azienda) {
        if (azienda == null) {
            return;
        }
        String titolo = evento == null || evento.getTitolo() == null ? "evento" : evento.getTitolo();
        String messaggio = "Sei stato invitato come espositore all'evento: " + titolo + ".";
        notificaService.inviaNotifica(null, messaggio);
    }
}
