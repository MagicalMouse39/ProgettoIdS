package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.AziendaRequest;
import it.unicam.cs.diciotto.progettoids.dto.LoginRequest;
import it.unicam.cs.diciotto.progettoids.dto.SedeRequest;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Ruolo;
import it.unicam.cs.diciotto.progettoids.entity.RuoloUtente;
import it.unicam.cs.diciotto.progettoids.entity.Sede;
import it.unicam.cs.diciotto.progettoids.entity.StatoRegistrazione;
import it.unicam.cs.diciotto.progettoids.entity.Utente;
import it.unicam.cs.diciotto.progettoids.repository.AziendaRepository;
import it.unicam.cs.diciotto.progettoids.repository.UtenteRepository;
import it.unicam.cs.diciotto.progettoids.repository.PinRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GestoreUtenzaService {
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;
    private final INotificaService notificaService;
    private final PinRepository pinRepository;

    public GestoreUtenzaService(
            UtenteRepository utenteRepository,
            AziendaRepository aziendaRepository,
            INotificaService notificaService,
            PinRepository pinRepository) {
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
        this.notificaService = notificaService;
        this.pinRepository = pinRepository;
    }

    public Utente login(LoginRequest request) {
        return utenteRepository.findByEmail(request.getEmail())
                .filter(utente -> utente.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Credenziali non valide"));
    }

    public Azienda registraAzienda(AziendaRequest request) {
        if (request.getSedi() == null || request.getSedi().isEmpty()) {
            throw new IllegalArgumentException("Almeno una sede è obbligatoria");
        }
        Azienda azienda = new Azienda();
        azienda.setSedi(mapSedi(request.getSedi()));

        azienda.setRagioneSociale(request.getRagioneSociale());
        azienda.setPartitaIva(request.getPartitaIva());
        azienda.setDescrizioneAzienda(request.getDescrizioneAzienda());
        azienda.setLogo(request.getLogo());
        if (request.getUtenteId() != null) {
            Utente utente = utenteRepository.findById(request.getUtenteId())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
            azienda.setUtenteRichiedente(utente);
        }
        azienda.setStato(StatoRegistrazione.IN_ATTESA);
        return aziendaRepository.save(azienda);
    }

    public Azienda modificaDatiAzienda(Long aziendaId, AziendaRequest request) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        azienda.setRagioneSociale(request.getRagioneSociale());
        azienda.setPartitaIva(request.getPartitaIva());
        azienda.setDescrizioneAzienda(request.getDescrizioneAzienda());
        azienda.setLogo(request.getLogo());
        if (request.getUtenteId() != null) {
            Utente utente = utenteRepository.findById(request.getUtenteId())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
            azienda.setUtenteRichiedente(utente);
        }
        if (request.getSedi() != null) {
            if (request.getSedi().isEmpty()) {
                throw new IllegalArgumentException("Almeno una sede è obbligatoria");
            }
            azienda.setSedi(mapSedi(request.getSedi()));
        }
        azienda.setStato(StatoRegistrazione.IN_ATTESA);
        return aziendaRepository.save(azienda);
    }

    public Azienda approvaRichiestaAzienda(Long aziendaId, RuoloUtente ruoloDaAssegnare) {
        if (aziendaRepository.findByStato(StatoRegistrazione.IN_ATTESA).isEmpty()) {
            throw new IllegalArgumentException("Nessuna richiesta pendente");
        }
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        azienda.setStato(StatoRegistrazione.APPROVATA);
        if (ruoloDaAssegnare != null && azienda.getUtenteRichiedente() != null) {
            Ruolo ruolo = new Ruolo();
            ruolo.setTipoRuolo(ruoloDaAssegnare);
            azienda.getUtenteRichiedente().getRuoli().add(ruolo);
            utenteRepository.save(azienda.getUtenteRichiedente());
        }
        notificaService.inviaNotifica(
                azienda.getUtenteRichiedente(),
                "Richiesta azienda approvata.");
        return aziendaRepository.save(azienda);
    }

    public Azienda respingiRichiestaAzienda(Long aziendaId, String motivazione) {
        if (aziendaRepository.findByStato(StatoRegistrazione.IN_ATTESA).isEmpty()) {
            throw new IllegalArgumentException("Nessuna richiesta pendente");
        }
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        azienda.setStato(StatoRegistrazione.RESPINTA);
        notificaService.inviaNotifica(
                azienda.getUtenteRichiedente(),
                "Richiesta azienda respinta: " + motivazione);
        return aziendaRepository.save(azienda);
    }

    public List<Azienda> getListaAziende() {
        return aziendaRepository.findAll();
    }

    public List<Azienda> getRichiesteRegistrazione() {
        return aziendaRepository.findByStato(StatoRegistrazione.IN_ATTESA);
    }

    public List<it.unicam.cs.diciotto.progettoids.entity.Pin> getMappaAziende() {
        throw new IllegalStateException("Servizio attualmente non disponibile");
    }

    private List<Sede> mapSedi(List<SedeRequest> sediRequest) {
        List<Sede> sedi = new ArrayList<>();
        for (SedeRequest request : sediRequest) {
            Sede sede = new Sede(request.getIndirizzo(), request.getLatitudine(), request.getLongitudine());
            sede.setComune(request.getComune());
            sede.setCap(request.getCap());
            sedi.add(sede);
        }
        return sedi;
    }
}
