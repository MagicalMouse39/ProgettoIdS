package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.ApiDtoMapper;
import it.unicam.cs.diciotto.progettoids.dto.EventoRequest;
import it.unicam.cs.diciotto.progettoids.dto.response.EventoResponse;
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
    public EventoResponse creaEvento(@RequestBody EventoRequest request) {
        return ApiDtoMapper.toEventoResponse(gestoreEventiService.creaEvento(request));
    }

    @PutMapping("/{id}")
    public EventoResponse modificaEvento(@PathVariable Long id, @RequestBody EventoRequest request) {
        return ApiDtoMapper.toEventoResponse(gestoreEventiService.modificaEvento(id, request));
    }

    @PostMapping("/{id}/invita")
    public EventoResponse invitaEspositore(@PathVariable Long id, @RequestParam Long aziendaId) {
        return ApiDtoMapper.toEventoResponse(gestoreEventiService.invitaEspositore(id, aziendaId));
    }

    @PostMapping("/{id}/prenota")
    public EventoResponse prenotaEvento(@PathVariable Long id, @RequestParam Long utenteId) {
        return ApiDtoMapper.toEventoResponse(gestoreEventiService.prenotaEvento(id, utenteId));
    }

    @GetMapping
    public List<EventoResponse> listaEventi() {
        return ApiDtoMapper.toEventoResponses(gestoreEventiService.getListaEventi());
    }
}
