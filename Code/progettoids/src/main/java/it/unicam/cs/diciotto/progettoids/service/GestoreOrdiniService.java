package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.CarrelloAddRequest;
import it.unicam.cs.diciotto.progettoids.dto.CheckoutRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoDecisioneRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoRequest;
import it.unicam.cs.diciotto.progettoids.entity.Carrello;
import it.unicam.cs.diciotto.progettoids.entity.DettaglioOrdine;
import it.unicam.cs.diciotto.progettoids.entity.Ordine;
import it.unicam.cs.diciotto.progettoids.entity.Prodotto;
import it.unicam.cs.diciotto.progettoids.entity.StatoOrdine;
import it.unicam.cs.diciotto.progettoids.repository.CarrelloRepository;
import it.unicam.cs.diciotto.progettoids.repository.DettaglioOrdineRepository;
import it.unicam.cs.diciotto.progettoids.repository.OrdineRepository;
import it.unicam.cs.diciotto.progettoids.repository.ProdottoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GestoreOrdiniService {
    private final CarrelloRepository carrelloRepository;
    private final ProdottoRepository prodottoRepository;
    private final DettaglioOrdineRepository dettaglioOrdineRepository;
    private final OrdineRepository ordineRepository;

    public GestoreOrdiniService(
            CarrelloRepository carrelloRepository,
            ProdottoRepository prodottoRepository,
            DettaglioOrdineRepository dettaglioOrdineRepository,
            OrdineRepository ordineRepository) {
        this.carrelloRepository = carrelloRepository;
        this.prodottoRepository = prodottoRepository;
        this.dettaglioOrdineRepository = dettaglioOrdineRepository;
        this.ordineRepository = ordineRepository;
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
        DettaglioOrdine dettaglio = new DettaglioOrdine();
        dettaglio.setProdottoRiferimento(prodotto);
        dettaglio.setQuantita(request.getQuantita());
        dettaglio.setPrezzoUnitarioSnapshot(prodotto.getPrezzo());
        dettaglioOrdineRepository.save(dettaglio);
        carrello.getDettagli().add(dettaglio);
        return carrelloRepository.save(carrello);
    }

    public Ordine checkout(CheckoutRequest request) {
        Carrello carrello = carrelloRepository.findById(request.getCarrelloId())
                .orElseThrow(() -> new IllegalArgumentException("Carrello non trovato"));
        Ordine ordine = new Ordine();
        ordine.setDataCreazione(LocalDate.now().toString());
        ordine.setIndirizzoConsegna(request.getIndirizzoConsegna());
        ordine.setStato(StatoOrdine.IN_LAVORAZIONE);
        ordine.setDettagli(carrello.getDettagli());
        double totale = carrello.getDettagli().stream()
                .mapToDouble(d -> d.getPrezzoUnitarioSnapshot() * d.getQuantita())
                .sum();
        ordine.setTotalePagato(totale);
        return ordineRepository.save(ordine);
    }

    public Ordine richiediRimborso(RimborsoRequest request) {
        Ordine ordine = ordineRepository.findById(request.getOrdineId())
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
        ordine.setStato(StatoOrdine.RICHIESTA_RIMBORSO);
        ordine.setMotivazioneRimborso(request.getMotivazione());
        return ordineRepository.save(ordine);
    }

    public Ordine approvaRimborso(RimborsoDecisioneRequest request) {
        Ordine ordine = ordineRepository.findById(request.getOrdineId())
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
        ordine.setStato(StatoOrdine.RIMBORSATO);
        ordine.setNoteAmministratore(request.getNoteAmministratore());
        return ordineRepository.save(ordine);
    }

    public Ordine respingiRimborso(RimborsoDecisioneRequest request) {
        Ordine ordine = ordineRepository.findById(request.getOrdineId())
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
        ordine.setStato(StatoOrdine.RIMBORSO_RESPINTO);
        ordine.setNoteAmministratore(request.getNoteAmministratore());
        return ordineRepository.save(ordine);
    }

    public List<Ordine> getOrdiniPerStato(StatoOrdine stato) {
        return ordineRepository.findByStato(stato);
    }
}
