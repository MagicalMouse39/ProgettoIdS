package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.CarrelloAddRequest;
import it.unicam.cs.diciotto.progettoids.dto.CheckoutRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoDecisioneRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoRequest;
import it.unicam.cs.diciotto.progettoids.entity.*;
import it.unicam.cs.diciotto.progettoids.repository.CarrelloRepository;
import it.unicam.cs.diciotto.progettoids.repository.OrdineRepository;
import it.unicam.cs.diciotto.progettoids.repository.ProdottoRepository;
import it.unicam.cs.diciotto.progettoids.repository.UtenteRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GestoreOrdiniService {
    private final CarrelloRepository carrelloRepository;
    private final ProdottoRepository prodottoRepository;
    private final OrdineRepository ordineRepository;
    private final INotificaService notificaService;
    private final UtenteRepository utenteRepository;

    public GestoreOrdiniService(
            CarrelloRepository carrelloRepository,
            ProdottoRepository prodottoRepository,
            OrdineRepository ordineRepository,
            INotificaService notificaService,
            UtenteRepository utenteRepository) {
        this.carrelloRepository = carrelloRepository;
        this.prodottoRepository = prodottoRepository;
        this.ordineRepository = ordineRepository;
        this.notificaService = notificaService;
        this.utenteRepository = utenteRepository;
    }

    public Carrello creaCarrello() {
        Carrello carrello = new Carrello();
        carrello.setDataCreazione(LocalDate.now().toString());
        return carrelloRepository.save(carrello);
    }

    public Carrello aggiungiProdotto(CarrelloAddRequest request) {
        Carrello carrello = carrelloRepository.findById(request.getCarrelloId())
                .orElseThrow(() -> new IllegalArgumentException("Carrello non trovato"));
        Prodotto prodotto = prodottoRepository.findById(request.getProdottoId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        int quantita = Math.max(1, request.getQuantita());
        carrello.aggiungiProdotto(prodotto, quantita);
        return carrelloRepository.save(carrello);
    }

    public Ordine checkout(CheckoutRequest request) {
        Carrello carrello = carrelloRepository.findById(request.getCarrelloId())
                .orElseThrow(() -> new IllegalArgumentException("Carrello non trovato"));
        if (carrello.getProdotti() == null || carrello.getProdotti().isEmpty()) {
            throw new IllegalArgumentException("Carrello vuoto");
        }
        for (var entry : carrello.getProdotti().entrySet()) {
            Prodotto prodotto = entry.getKey();
            Integer quantita = entry.getValue();
            if (prodotto == null || quantita == null) {
                continue;
            }
            if (prodotto.getQuantitaDisponibile() < quantita) {
                throw new IllegalArgumentException("Quantita disponibile insufficiente per il prodotto");
            }
        }
        Ordine ordine = new Ordine(carrello);
        ordine.setDataCreazione(LocalDate.now().toString());
        ordine.setIndirizzoConsegna(request.getIndirizzoConsegna());
        ordine.setStato(StatoOrdine.IN_LAVORAZIONE);
        if (request.getUtenteId() != null) {
            Utente utente = utenteRepository.findById(request.getUtenteId())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
            ordine.setUtente(utente);
        }
        carrello.svuota();
        return ordineRepository.save(ordine);
    }

    public Ordine richiediRimborso(RimborsoRequest request) {
        if (ordineRepository.findByStato(StatoOrdine.COMPLETATO).isEmpty()) {
            throw new IllegalArgumentException("Nessun ordine completato");
        }
        Ordine ordine = ordineRepository.findById(request.getOrdineId())
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
        ordine.setStato(StatoOrdine.RICHIESTA_RIMBORSO);
        ordine.setMotivazioneRimborso(request.getMotivazione());
        Ordine saved = ordineRepository.save(ordine);
        notificaService.inviaNotifica(saved.getUtente(), "Richiesta rimborso inviata.");
        return saved;
    }

    public Ordine approvaRimborso(RimborsoDecisioneRequest request) {
        if (ordineRepository.findByStato(StatoOrdine.RICHIESTA_RIMBORSO).isEmpty()) {
            throw new IllegalArgumentException("Nessuna richiesta di rimborso");
        }
        Ordine ordine = ordineRepository.findById(request.getOrdineId())
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
        ordine.setStato(StatoOrdine.RIMBORSATO);
        Ordine saved = ordineRepository.save(ordine);
        notificaRimborso(saved, true);
        return saved;
    }

    public Ordine respingiRimborso(RimborsoDecisioneRequest request) {
        if (ordineRepository.findByStato(StatoOrdine.RICHIESTA_RIMBORSO).isEmpty()) {
            throw new IllegalArgumentException("Nessuna richiesta di rimborso");
        }
        Ordine ordine = ordineRepository.findById(request.getOrdineId())
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
        ordine.setNoteAmministratore(request.getNoteAmministratore());
        ordine.setStato(StatoOrdine.RIMBORSO_RESPINTO);
        Ordine saved = ordineRepository.save(ordine);
        notificaRimborso(saved, false);
        return saved;
    }

    public List<Ordine> getOrdiniPerStato(StatoOrdine stato) {
        return ordineRepository.findByStato(stato);
    }

    public List<Ordine> getOrdiniPerStatoEUtente(StatoOrdine stato, Long utenteId) {
        return ordineRepository.findByStatoAndUtente_Id(stato, utenteId);
    }

    private void notificaRimborso(Ordine ordine, boolean approvato) {
        if (ordine == null) {
            return;
        }
        String messaggio = approvato
                ? "Il rimborso per l'ordine è stato approvato."
                : "Il rimborso per l'ordine è stato respinto.";
        notificaService.inviaNotifica(ordine.getUtente(), messaggio);
    }
}
