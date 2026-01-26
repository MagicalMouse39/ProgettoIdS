package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.EventoRequest;
import it.unicam.cs.diciotto.progettoids.entity.Evento;
import it.unicam.cs.diciotto.progettoids.service.GestoreEventiService;
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
@RequestMapping("/api/eventi")
public class GestoreEventiController {
    private final GestoreEventiService gestoreEventiService;

    public GestoreEventiController(GestoreEventiService gestoreEventiService) {
        this.gestoreEventiService = gestoreEventiService;
    }

    @PostMapping
    public Evento creaEvento(@RequestBody EventoRequest request) {
        return gestoreEventiService.creaEvento(request);
    }

    @PutMapping("/{id}")
    public Evento modificaEvento(@PathVariable Long id, @RequestBody EventoRequest request) {
        return gestoreEventiService.modificaEvento(id, request);
    }

    @PostMapping("/{id}/invita")
    public Evento invitaEspositore(@PathVariable Long id, @RequestParam Long aziendaId) {
        return gestoreEventiService.invitaEspositore(id, aziendaId);
    }

    @PostMapping("/{id}/prenota")
    public Evento prenotaEvento(@PathVariable Long id, @RequestParam Long utenteId) {
        return gestoreEventiService.prenotaEvento(id, utenteId);
    }

    @GetMapping
    public List<Evento> listaEventi() {
        return gestoreEventiService.getListaEventi();
    }
}
