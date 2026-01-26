package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.IngredienteRequest;
import it.unicam.cs.diciotto.progettoids.dto.PacchettoRequest;
import it.unicam.cs.diciotto.progettoids.dto.ProcessoRequest;
import it.unicam.cs.diciotto.progettoids.dto.TipicitaRequest;
import it.unicam.cs.diciotto.progettoids.entity.Prodotto;
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
    public Prodotto creaIngrediente(@RequestBody IngredienteRequest request) {
        return gestoreCatalogoService.creaIngrediente(request);
    }

    @PostMapping("/processi")
    public Prodotto creaProcesso(@RequestBody ProcessoRequest request) {
        return gestoreCatalogoService.creaProcesso(request);
    }

    @PostMapping("/tipicita")
    public Prodotto creaTipicita(@RequestBody TipicitaRequest request) {
        return gestoreCatalogoService.creaTipicita(request);
    }

    @PostMapping("/pacchetti")
    public Prodotto creaPacchetto(@RequestBody PacchettoRequest request) {
        return gestoreCatalogoService.creaPacchetto(request);
    }

    @PutMapping("/prodotti/{id}/approva")
    public Prodotto approvaProdotto(@PathVariable Long id) {
        return gestoreCatalogoService.approvaProdotto(id);
    }

    @PutMapping("/prodotti/{id}/respingi")
    public Prodotto respingiProdotto(@PathVariable Long id) {
        return gestoreCatalogoService.respingiProdotto(id);
    }

    @PutMapping("/prodotti/{id}/inventario")
    public Prodotto aggiornaInventario(@PathVariable Long id, @RequestParam int quantita) {
        return gestoreCatalogoService.aggiornaInventario(id, quantita);
    }

    @GetMapping("/prodotti/approvati")
    public List<Prodotto> prodottiApprovati() {
        return gestoreCatalogoService.getProdottiApprovati();
    }

    @GetMapping("/prodotti/in-attesa")
    public List<Prodotto> prodottiInAttesa() {
        return gestoreCatalogoService.getProdottiInAttesa();
    }

    @GetMapping("/prodotti/azienda/{aziendaId}")
    public List<Prodotto> prodottiDiAzienda(@PathVariable Long aziendaId) {
        return gestoreCatalogoService.getProdottiDiAzienda(aziendaId);
    }

    @GetMapping("/prodotti/cerca")
    public List<Prodotto> cercaPerZona(@RequestParam String zona) {
        return gestoreCatalogoService.cercaPerZona(zona);
    }

    @PostMapping("/prodotti/{id}/social")
    public void pubblicaSuSocial(@PathVariable Long id, @RequestParam Long aziendaId, @RequestParam String testo) {
        gestoreCatalogoService.pubblicaProdottoSuSocial(aziendaId, id, testo);
    }
}
