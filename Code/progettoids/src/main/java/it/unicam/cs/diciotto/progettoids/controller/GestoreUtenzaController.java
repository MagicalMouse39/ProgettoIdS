package it.unicam.cs.diciotto.progettoids.controller;

import it.unicam.cs.diciotto.progettoids.dto.ApiDtoMapper;
import it.unicam.cs.diciotto.progettoids.dto.AziendaRequest;
import it.unicam.cs.diciotto.progettoids.dto.LoginRequest;
import it.unicam.cs.diciotto.progettoids.dto.response.AziendaResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.UtenteResponse;
import it.unicam.cs.diciotto.progettoids.entity.RuoloUtente;
import it.unicam.cs.diciotto.progettoids.service.GestoreUtenzaService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utenza")
public class GestoreUtenzaController {
    private final GestoreUtenzaService gestoreUtenzaService;

    public GestoreUtenzaController(GestoreUtenzaService gestoreUtenzaService) {
        this.gestoreUtenzaService = gestoreUtenzaService;
    }

    @PostMapping("/login")
    public UtenteResponse login(@RequestBody LoginRequest request) {
        return ApiDtoMapper.toUtenteResponse(gestoreUtenzaService.login(request));
    }

    @PostMapping("/aziende")
    public AziendaResponse registraAzienda(@RequestBody AziendaRequest request) {
        return ApiDtoMapper.toAziendaResponse(gestoreUtenzaService.registraAzienda(request));
    }

    @PutMapping("/aziende/{id}")
    public AziendaResponse modificaAzienda(@PathVariable Long id, @RequestBody AziendaRequest request) {
        return ApiDtoMapper.toAziendaResponse(gestoreUtenzaService.modificaDatiAzienda(id, request));
    }

    @PutMapping("/aziende/{id}/approva")
    public AziendaResponse approvaAzienda(
            @PathVariable Long id,
            @RequestParam RuoloUtente ruolo) {
        return ApiDtoMapper.toAziendaResponse(gestoreUtenzaService.approvaRichiestaAzienda(id, ruolo));
    }

    @PutMapping("/aziende/{id}/respingi")
    public AziendaResponse respingiAzienda(
            @PathVariable Long id,
            @RequestParam String motivazione) {
        return ApiDtoMapper.toAziendaResponse(gestoreUtenzaService.respingiRichiestaAzienda(id, motivazione));
    }

    @GetMapping("/aziende")
    public List<AziendaResponse> listaAziende() {
        return ApiDtoMapper.toAziendaResponses(gestoreUtenzaService.getListaAziende());
    }

    @GetMapping("/aziende/pending")
    public List<AziendaResponse> richiesteRegistrazione() {
        return ApiDtoMapper.toAziendaResponses(gestoreUtenzaService.getRichiesteRegistrazione());
    }

    @GetMapping("/aziende/mappa")
    public List<it.unicam.cs.diciotto.progettoids.dto.response.PinResponse> mappaAziende() {
        return ApiDtoMapper.toPinResponses(gestoreUtenzaService.getMappaAziende());
    }
}
