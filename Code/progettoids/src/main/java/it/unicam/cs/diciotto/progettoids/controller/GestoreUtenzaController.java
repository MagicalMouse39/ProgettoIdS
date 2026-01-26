package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.AziendaRequest;
import it.unicam.cs.diciotto.progettoids.dto.LoginRequest;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Utente;
import it.unicam.cs.diciotto.progettoids.service.GestoreUtenzaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utenza")
public class GestoreUtenzaController {
    private final GestoreUtenzaService gestoreUtenzaService;

    public GestoreUtenzaController(GestoreUtenzaService gestoreUtenzaService) {
        this.gestoreUtenzaService = gestoreUtenzaService;
    }

    @PostMapping("/login")
    public Utente login(@RequestBody LoginRequest request) {
        return gestoreUtenzaService.login(request);
    }

    @PostMapping("/aziende")
    public Azienda registraAzienda(@RequestBody AziendaRequest request) {
        return gestoreUtenzaService.registraAzienda(request);
    }

    @PutMapping("/aziende/{id}")
    public Azienda modificaAzienda(@PathVariable Long id, @RequestBody AziendaRequest request) {
        return gestoreUtenzaService.modificaDatiAzienda(id, request);
    }

    @PutMapping("/aziende/{id}/approva")
    public Azienda approvaAzienda(@PathVariable Long id) {
        return gestoreUtenzaService.approvaRichiestaAzienda(id);
    }

    @PutMapping("/aziende/{id}/respingi")
    public Azienda respingiAzienda(@PathVariable Long id) {
        return gestoreUtenzaService.respingiRichiestaAzienda(id);
    }

    @GetMapping("/aziende")
    public List<Azienda> listaAziende() {
        return gestoreUtenzaService.getListaAziende();
    }

    @GetMapping("/aziende/pending")
    public List<Azienda> richiesteRegistrazione() {
        return gestoreUtenzaService.getRichiesteRegistrazione();
    }
}
