package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.CarrelloAddRequest;
import it.unicam.cs.diciotto.progettoids.dto.CheckoutRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoDecisioneRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoRequest;
import it.unicam.cs.diciotto.progettoids.entity.Carrello;
import it.unicam.cs.diciotto.progettoids.entity.Ordine;
import it.unicam.cs.diciotto.progettoids.entity.StatoOrdine;
import it.unicam.cs.diciotto.progettoids.service.GestoreOrdiniService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordini")
public class GestoreOrdiniController {
    private final GestoreOrdiniService gestoreOrdiniService;

    public GestoreOrdiniController(GestoreOrdiniService gestoreOrdiniService) {
        this.gestoreOrdiniService = gestoreOrdiniService;
    }

    @PostMapping("/carrelli")
    public Carrello creaCarrello() {
        return gestoreOrdiniService.creaCarrello();
    }

    @PostMapping("/carrelli/aggiungi")
    public Carrello aggiungiProdotto(@RequestBody CarrelloAddRequest request) {
        return gestoreOrdiniService.aggiungiProdotto(request);
    }

    @PostMapping("/checkout")
    public Ordine checkout(@RequestBody CheckoutRequest request) {
        return gestoreOrdiniService.checkout(request);
    }

    @PostMapping("/rimborso/richiesta")
    public Ordine richiediRimborso(@RequestBody RimborsoRequest request) {
        return gestoreOrdiniService.richiediRimborso(request);
    }

    @PostMapping("/rimborso/approva")
    public Ordine approvaRimborso(@RequestBody RimborsoDecisioneRequest request) {
        return gestoreOrdiniService.approvaRimborso(request);
    }

    @PostMapping("/rimborso/respingi")
    public Ordine respingiRimborso(@RequestBody RimborsoDecisioneRequest request) {
        return gestoreOrdiniService.respingiRimborso(request);
    }

    @GetMapping
    public List<Ordine> ordiniPerStato(@RequestParam StatoOrdine stato) {
        return gestoreOrdiniService.getOrdiniPerStato(stato);
    }
}
