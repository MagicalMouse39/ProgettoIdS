package it.unicam.cs.diciotto.progettoids.dto;

import it.unicam.cs.diciotto.progettoids.dto.response.AziendaResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.CarrelloResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.DettaglioOrdineResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.EventoResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.OrdineResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.PinResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.ProdottoResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.RuoloResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.SedeResponse;
import it.unicam.cs.diciotto.progettoids.dto.response.UtenteResponse;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Carrello;
import it.unicam.cs.diciotto.progettoids.entity.DettaglioOrdine;
import it.unicam.cs.diciotto.progettoids.entity.Evento;
import it.unicam.cs.diciotto.progettoids.entity.Ingrediente;
import it.unicam.cs.diciotto.progettoids.entity.Ordine;
import it.unicam.cs.diciotto.progettoids.entity.Pacchetto;
import it.unicam.cs.diciotto.progettoids.entity.Pin;
import it.unicam.cs.diciotto.progettoids.entity.Processo;
import it.unicam.cs.diciotto.progettoids.entity.Prodotto;
import it.unicam.cs.diciotto.progettoids.entity.Ruolo;
import it.unicam.cs.diciotto.progettoids.entity.Sede;
import it.unicam.cs.diciotto.progettoids.entity.Tipicita;
import it.unicam.cs.diciotto.progettoids.entity.Utente;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ApiDtoMapper {
    private ApiDtoMapper() {}

    public static AziendaResponse toAziendaResponse(Azienda azienda) {
        if (azienda == null) {
            return null;
        }
        AziendaResponse response = new AziendaResponse();
        response.setId(azienda.getId());
        response.setRagioneSociale(azienda.getRagioneSociale());
        response.setPartitaIva(azienda.getPartitaIva());
        response.setDescrizioneAzienda(azienda.getDescrizioneAzienda());
        response.setLogo(azienda.getLogo());
        response.setStato(azienda.getStato());
        response.setUtenteId(azienda.getUtenteRichiedente() == null ? null : azienda.getUtenteRichiedente().getId());
        response.setSedi(toSedeResponses(azienda.getSedi()));
        return response;
    }

    public static List<AziendaResponse> toAziendaResponses(List<Azienda> aziende) {
        if (aziende == null) {
            return Collections.emptyList();
        }
        return aziende.stream().map(ApiDtoMapper::toAziendaResponse).collect(Collectors.toList());
    }

    public static SedeResponse toSedeResponse(Sede sede) {
        if (sede == null) {
            return null;
        }
        SedeResponse response = new SedeResponse();
        response.setId(sede.getId());
        response.setIndirizzo(sede.getIndirizzo());
        response.setComune(sede.getComune());
        response.setCap(sede.getCap());
        response.setLatitudine(sede.getLatitudine());
        response.setLongitudine(sede.getLongitudine());
        return response;
    }

    public static List<SedeResponse> toSedeResponses(List<Sede> sedi) {
        if (sedi == null) {
            return Collections.emptyList();
        }
        return sedi.stream().map(ApiDtoMapper::toSedeResponse).collect(Collectors.toList());
    }

    public static ProdottoResponse toProdottoResponse(Prodotto prodotto) {
        if (prodotto == null) {
            return null;
        }
        ProdottoResponse response = new ProdottoResponse();
        response.setId(prodotto.getId());
        response.setNome(prodotto.getNome());
        response.setDescrizione(prodotto.getDescrizione());
        response.setProvenienza(prodotto.getProvenienza());
        response.setImmagine(prodotto.getImmagine());
        response.setPrezzo(prodotto.getPrezzo());
        response.setQuantitaDisponibile(prodotto.getQuantitaDisponibile());
        response.setDataScadenza(prodotto.getDataScadenza());
        response.setStatoCorrente(prodotto.getStatoCorrente());
        response.setProduttoreId(prodotto.getProduttore() == null ? null : prodotto.getProduttore().getId());

        if (prodotto instanceof Ingrediente ingrediente) {
            response.setTipo("INGREDIENTE");
            response.setCertificazioni(ingrediente.getCertificazioni());
        } else if (prodotto instanceof Processo processo) {
            response.setTipo("PROCESSO");
            response.setDescrizioneFasi(processo.getDescrizioneFasi());
            response.setIngredientiIds(processo.getIngredientiUsati() == null
                    ? Collections.emptyList()
                    : processo.getIngredientiUsati().stream()
                            .filter(i -> i != null && i.getId() != null)
                            .map(Ingrediente::getId)
                            .collect(Collectors.toList()));
        } else if (prodotto instanceof Tipicita tipicita) {
            response.setTipo("TIPICITA");
            response.setCertificazioni(tipicita.getCertificazioni());
            if (tipicita.getProcessoProduttivo() != null) {
                response.setProcessoId(tipicita.getProcessoProduttivo().getId());
            }
        } else if (prodotto instanceof Pacchetto pacchetto) {
            response.setTipo("PACCHETTO");
            response.setTipicitaIds(pacchetto.getTipicita() == null
                    ? Collections.emptyList()
                    : pacchetto.getTipicita().stream()
                            .filter(t -> t != null && t.getId() != null)
                            .map(Tipicita::getId)
                            .collect(Collectors.toList()));
        } else {
            response.setTipo("PRODOTTO");
        }

        return response;
    }

    public static List<ProdottoResponse> toProdottoResponses(List<Prodotto> prodotti) {
        if (prodotti == null) {
            return Collections.emptyList();
        }
        return prodotti.stream().map(ApiDtoMapper::toProdottoResponse).collect(Collectors.toList());
    }

    public static CarrelloResponse toCarrelloResponse(Carrello carrello) {
        if (carrello == null) {
            return null;
        }
        CarrelloResponse response = new CarrelloResponse();
        response.setId(carrello.getId());
        response.setDataCreazione(carrello.getDataCreazione());
        response.setProdottoQuantita(carrello.getProdotti() == null
                ? Collections.emptyMap()
                : carrello.getProdotti().entrySet().stream()
                        .filter(entry -> entry.getKey() != null && entry.getKey().getId() != null)
                        .filter(entry -> entry.getValue() != null)
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().getId(),
                                java.util.Map.Entry::getValue)));
        response.setTotale(carrello.getTotale());
        return response;
    }

    public static OrdineResponse toOrdineResponse(Ordine ordine) {
        if (ordine == null) {
            return null;
        }
        OrdineResponse response = new OrdineResponse();
        response.setId(ordine.getId());
        response.setDataCreazione(ordine.getDataCreazione());
        response.setIndirizzoConsegna(ordine.getIndirizzoConsegna());
        response.setStato(ordine.getStato());
        response.setTotalePagato(ordine.getTotalePagato());
        response.setMotivazioneRimborso(ordine.getMotivazioneRimborso());
        response.setNoteAmministratore(ordine.getNoteAmministratore());
        response.setUtenteId(ordine.getUtente() == null ? null : ordine.getUtente().getId());
        response.setDettagli(ordine.getDettagli() == null
                ? Collections.emptyList()
                : ordine.getDettagli().stream().map(ApiDtoMapper::toDettaglioOrdineResponse).collect(Collectors.toList()));
        return response;
    }

    public static List<OrdineResponse> toOrdineResponses(List<Ordine> ordini) {
        if (ordini == null) {
            return Collections.emptyList();
        }
        return ordini.stream().map(ApiDtoMapper::toOrdineResponse).collect(Collectors.toList());
    }

    public static PinResponse toPinResponse(Pin pin) {
        if (pin == null) {
            return null;
        }
        PinResponse response = new PinResponse();
        response.setId(pin.getId());
        response.setLatitudine(pin.getLatitudine());
        response.setLongitudine(pin.getLongitudine());
        return response;
    }

    public static List<PinResponse> toPinResponses(List<Pin> pins) {
        if (pins == null) {
            return Collections.emptyList();
        }
        return pins.stream().map(ApiDtoMapper::toPinResponse).collect(Collectors.toList());
    }

    public static DettaglioOrdineResponse toDettaglioOrdineResponse(DettaglioOrdine dettaglio) {
        if (dettaglio == null) {
            return null;
        }
        DettaglioOrdineResponse response = new DettaglioOrdineResponse();
        response.setId(dettaglio.getId());
        response.setQuantita(dettaglio.getQuantita());
        response.setPrezzoUnitarioSnapshot(dettaglio.getPrezzoUnitarioSnapshot());
        response.setProdottoId(dettaglio.getProdottoRiferimento() == null
                ? null
                : dettaglio.getProdottoRiferimento().getId());
        return response;
    }

    public static EventoResponse toEventoResponse(Evento evento) {
        if (evento == null) {
            return null;
        }
        EventoResponse response = new EventoResponse();
        response.setId(evento.getId());
        response.setTitolo(evento.getTitolo());
        response.setDataInizio(evento.getDataInizio());
        response.setDataFine(evento.getDataFine());
        response.setMaxPartecipanti(evento.getMaxPartecipanti());
        response.setIscrittiAttuali(evento.getIscrittiAttuali());
        response.setLuogoId(evento.getLuogo() == null ? null : evento.getLuogo().getId());
        response.setEspositoriIds(toIdSet(evento.getEspositori()));
        response.setPartecipantiIds(toIdSet(evento.getPartecipanti()));
        return response;
    }

    public static List<EventoResponse> toEventoResponses(List<Evento> eventi) {
        if (eventi == null) {
            return Collections.emptyList();
        }
        return eventi.stream().map(ApiDtoMapper::toEventoResponse).collect(Collectors.toList());
    }

    public static UtenteResponse toUtenteResponse(Utente utente) {
        if (utente == null) {
            return null;
        }
        UtenteResponse response = new UtenteResponse();
        response.setId(utente.getId());
        response.setEmail(utente.getEmail());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        Set<RuoloResponse> ruoli = utente.getRuoli() == null
                ? Collections.emptySet()
                : utente.getRuoli().stream().map(ApiDtoMapper::toRuoloResponse).collect(Collectors.toSet());
        response.setRuoli(ruoli);
        return response;
    }

    public static RuoloResponse toRuoloResponse(Ruolo ruolo) {
        if (ruolo == null) {
            return null;
        }
        RuoloResponse response = new RuoloResponse();
        response.setId(ruolo.getId());
        response.setTipoRuolo(ruolo.getTipoRuolo());
        return response;
    }

    private static Set<Long> toIdSet(Set<?> entities) {
        if (entities == null) {
            return Collections.emptySet();
        }
        return entities.stream()
                .filter(e -> e != null)
                .map(e -> {
                    if (e instanceof Azienda azienda) {
                        return azienda.getId();
                    }
                    if (e instanceof Utente utente) {
                        return utente.getId();
                    }
                    return null;
                })
                .filter(id -> id != null)
                .collect(Collectors.toSet());
    }
}
