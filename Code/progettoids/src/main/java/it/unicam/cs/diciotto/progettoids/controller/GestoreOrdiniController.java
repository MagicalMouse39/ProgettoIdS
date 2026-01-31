package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.ApiDtoMapper;
import it.unicam.cs.diciotto.progettoids.dto.CarrelloAddRequest;
import it.unicam.cs.diciotto.progettoids.dto.CheckoutRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoDecisioneRequest;
import it.unicam.cs.diciotto.progettoids.dto.RimborsoRequest;
import it.unicam.cs.diciotto.progettoids.dto.response.CarrelloResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.OrdineResponse;
import it.unicam.cs.diciotto.progettoids.entity.StatoOrdine;
import it.unicam.cs.diciotto.progettoids.service.GestoreOrdiniService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordini")
public class GestoreOrdiniController {
    private final GestoreOrdiniService gestoreOrdiniService;

    public GestoreOrdiniController(GestoreOrdiniService gestoreOrdiniService) {
        this.gestoreOrdiniService = gestoreOrdiniService;
    }

    @PostMapping("/carrelli")
    public CarrelloResponse creaCarrello() {
        return ApiDtoMapper.toCarrelloResponse(gestoreOrdiniService.creaCarrello());
    }

    @PostMapping("/carrelli/aggiungi")
    public CarrelloResponse aggiungiProdotto(@RequestBody CarrelloAddRequest request) {
        return ApiDtoMapper.toCarrelloResponse(gestoreOrdiniService.aggiungiProdotto(request));
    }

    @PostMapping("/checkout")
    public OrdineResponse checkout(@RequestBody CheckoutRequest request) {
        return ApiDtoMapper.toOrdineResponse(gestoreOrdiniService.checkout(request));
    }

    @PostMapping("/rimborso/richiesta")
    public OrdineResponse richiediRimborso(@RequestBody RimborsoRequest request) {
        return ApiDtoMapper.toOrdineResponse(gestoreOrdiniService.richiediRimborso(request));
    }

    @PostMapping("/rimborso/approva")
    public OrdineResponse approvaRimborso(@RequestBody RimborsoDecisioneRequest request) {
        return ApiDtoMapper.toOrdineResponse(gestoreOrdiniService.approvaRimborso(request));
    }

    @PostMapping("/rimborso/respingi")
    public OrdineResponse respingiRimborso(@RequestBody RimborsoDecisioneRequest request) {
        return ApiDtoMapper.toOrdineResponse(gestoreOrdiniService.respingiRimborso(request));
    }

    @GetMapping
    public List<OrdineResponse> ordiniPerStato(@RequestParam StatoOrdine stato) {
        return ApiDtoMapper.toOrdineResponses(gestoreOrdiniService.getOrdiniPerStato(stato));
    }

    @GetMapping("/utente/{utenteId}")
    public List<OrdineResponse> ordiniPerStatoEUtente(
            @PathVariable Long utenteId,
            @RequestParam StatoOrdine stato) {
        return ApiDtoMapper.toOrdineResponses(gestoreOrdiniService.getOrdiniPerStatoEUtente(stato, utenteId));
    }
}
