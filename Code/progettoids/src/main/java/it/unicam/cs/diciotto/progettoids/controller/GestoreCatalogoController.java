package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.ApiDtoMapper;
import it.unicam.cs.diciotto.progettoids.dto.IngredienteRequest;
import it.unicam.cs.diciotto.progettoids.dto.PacchettoRequest;
import it.unicam.cs.diciotto.progettoids.dto.ProcessoRequest;
import it.unicam.cs.diciotto.progettoids.dto.TipicitaRequest;
import it.unicam.cs.diciotto.progettoids.dto.response.ProdottoResponse;
import it.unicam.cs.diciotto.progettoids.service.GestoreCatalogoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogo")
public class GestoreCatalogoController {
    private final GestoreCatalogoService gestoreCatalogoService;

    public GestoreCatalogoController(GestoreCatalogoService gestoreCatalogoService) {
        this.gestoreCatalogoService = gestoreCatalogoService;
    }

    @PostMapping("/ingredienti")
    public ProdottoResponse creaIngrediente(@RequestBody IngredienteRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.creaIngrediente(request));
    }

    @PostMapping("/processi")
    public ProdottoResponse creaProcesso(@RequestBody ProcessoRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.creaProcesso(request));
    }

    @PostMapping("/tipicita")
    public ProdottoResponse creaTipicita(@RequestBody TipicitaRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.creaTipicita(request));
    }

    @PostMapping("/pacchetti")
    public ProdottoResponse creaPacchetto(@RequestBody PacchettoRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.creaPacchetto(request));
    }

    @PutMapping("/ingredienti/{id}")
    public ProdottoResponse modificaIngrediente(@PathVariable Long id, @RequestBody IngredienteRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.modificaIngrediente(id, request));
    }

    @PutMapping("/processi/{id}")
    public ProdottoResponse modificaProcesso(@PathVariable Long id, @RequestBody ProcessoRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.modificaProcesso(id, request));
    }

    @PutMapping("/tipicita/{id}")
    public ProdottoResponse modificaTipicita(@PathVariable Long id, @RequestBody TipicitaRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.modificaTipicita(id, request));
    }

    @PutMapping("/pacchetti/{id}")
    public ProdottoResponse modificaPacchetto(@PathVariable Long id, @RequestBody PacchettoRequest request) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.modificaPacchetto(id, request));
    }

    @PutMapping("/prodotti/{id}/approva")
    public ProdottoResponse approvaProdotto(@PathVariable Long id) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.approvaProdotto(id));
    }

    @PutMapping("/prodotti/{id}/respingi")
    public ProdottoResponse respingiProdotto(@PathVariable Long id, @RequestParam String ragione) {
        return ApiDtoMapper.toProdottoResponse(gestoreCatalogoService.respingiProdotto(id, ragione));
    }

    @PutMapping("/prodotti/{id}/inventario")
    public ProdottoResponse aggiornaInventario(
            @PathVariable Long id,
            @RequestParam int quantita,
            @RequestParam double prezzo) {
        return ApiDtoMapper.toProdottoResponse(
                gestoreCatalogoService.aggiornaInventario(id, quantita, prezzo));
    }

    @GetMapping("/prodotti/approvati")
    public List<ProdottoResponse> prodottiApprovati() {
        return ApiDtoMapper.toProdottoResponses(gestoreCatalogoService.getProdottiApprovati());
    }

    @GetMapping("/prodotti/in-attesa")
    public List<ProdottoResponse> prodottiInAttesa() {
        return ApiDtoMapper.toProdottoResponses(gestoreCatalogoService.getProdottiInAttesa());
    }

    @GetMapping("/prodotti/azienda/{aziendaId}")
    public List<ProdottoResponse> prodottiDiAzienda(@PathVariable Long aziendaId) {
        return ApiDtoMapper.toProdottoResponses(gestoreCatalogoService.getProdottiDiAzienda(aziendaId));
    }

    @GetMapping("/prodotti/cerca")
    public List<ProdottoResponse> cercaPerZona(
            @RequestParam double latitudine,
            @RequestParam double longitudine,
            @RequestParam double raggioKm) {
        return ApiDtoMapper.toProdottoResponses(
                gestoreCatalogoService.cercaPerZona(latitudine, longitudine, raggioKm));
    }

    @PostMapping("/prodotti/{id}/social")
    public void pubblicaSuSocial(
            @PathVariable Long id,
            @RequestParam Long aziendaId,
            @RequestParam String testo,
            @RequestParam String urlImmagine) {
        gestoreCatalogoService.creaContenuto(aziendaId, id, testo, urlImmagine);
    }
}
