package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.AziendaRequest;
import it.unicam.cs.diciotto.progettoids.dto.LoginRequest;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.StatoRegistrazione;
import it.unicam.cs.diciotto.progettoids.entity.Utente;
import it.unicam.cs.diciotto.progettoids.repository.AziendaRepository;
import it.unicam.cs.diciotto.progettoids.repository.UtenteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GestoreUtenzaService {
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;

    public GestoreUtenzaService(UtenteRepository utenteRepository, AziendaRepository aziendaRepository) {
        this.utenteRepository = utenteRepository;
        this.aziendaRepository = aziendaRepository;
    }

    public Utente login(LoginRequest request) {
        return utenteRepository.findByEmail(request.getEmail())
                .filter(utente -> utente.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Credenziali non valide"));
    }

    public Azienda registraAzienda(AziendaRequest request) {
        Azienda azienda = new Azienda();
        azienda.setRagioneSociale(request.getRagioneSociale());
        azienda.setPartitaIva(request.getPartitaIva());
        azienda.setDescrizioneAzienda(request.getDescrizioneAzienda());
        azienda.setLogo(request.getLogo());
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
        return aziendaRepository.save(azienda);
    }

    public Azienda approvaRichiestaAzienda(Long aziendaId) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        azienda.setStato(StatoRegistrazione.APPROVATA);
        return aziendaRepository.save(azienda);
    }

    public Azienda respingiRichiestaAzienda(Long aziendaId) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        azienda.setStato(StatoRegistrazione.RESPINTA);
        return aziendaRepository.save(azienda);
    }

    public List<Azienda> getListaAziende() {
        return aziendaRepository.findAll();
    }

    public List<Azienda> getRichiesteRegistrazione() {
        return aziendaRepository.findByStato(StatoRegistrazione.IN_ATTESA);
    }
}
