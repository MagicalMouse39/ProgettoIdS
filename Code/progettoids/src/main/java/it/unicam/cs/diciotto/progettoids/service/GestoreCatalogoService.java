package it.unicam.cs.diciotto.progettoids.service;

import it.unicam.cs.diciotto.progettoids.dto.IngredienteRequest;
import it.unicam.cs.diciotto.progettoids.dto.PacchettoRequest;
import it.unicam.cs.diciotto.progettoids.dto.ProcessoRequest;
import it.unicam.cs.diciotto.progettoids.dto.TipicitaRequest;
import it.unicam.cs.diciotto.progettoids.entity.Azienda;
import it.unicam.cs.diciotto.progettoids.entity.Catalogo;
import it.unicam.cs.diciotto.progettoids.entity.Ingrediente;
import it.unicam.cs.diciotto.progettoids.entity.Pacchetto;
import it.unicam.cs.diciotto.progettoids.entity.Processo;
import it.unicam.cs.diciotto.progettoids.entity.Prodotto;
import it.unicam.cs.diciotto.progettoids.entity.StatoProdotto;
import it.unicam.cs.diciotto.progettoids.entity.Tipicita;
import it.unicam.cs.diciotto.progettoids.repository.AziendaRepository;
import it.unicam.cs.diciotto.progettoids.repository.CatalogoRepository;
import it.unicam.cs.diciotto.progettoids.repository.ProdottoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GestoreCatalogoService {
    private final ProdottoRepository prodottoRepository;
    private final AziendaRepository aziendaRepository;
    private final CatalogoRepository catalogoRepository;
    private final ISocialService socialService;
    private final INotificaService notificaService;

    public GestoreCatalogoService(
            ProdottoRepository prodottoRepository,
            AziendaRepository aziendaRepository,
            CatalogoRepository catalogoRepository,
            ISocialService socialService,
            INotificaService notificaService) {
        this.prodottoRepository = prodottoRepository;
        this.aziendaRepository = aziendaRepository;
        this.catalogoRepository = catalogoRepository;
        this.socialService = socialService;
        this.notificaService = notificaService;
    }

    public Ingrediente creaIngrediente(IngredienteRequest request) {
        Ingrediente ingrediente = new Ingrediente();
        applyCommonProdottoFields(ingrediente, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        ingrediente.setCertificazioni(request.getCertificazioni());
        ingrediente.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Ingrediente) prodottoRepository.save(ingrediente);
    }

    public Processo creaProcesso(ProcessoRequest request) {
        Processo processo = new Processo();
        applyCommonProdottoFields(processo, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        processo.setDescrizioneFasi(request.getDescrizioneFasi());
        setIngredientiProcesso(processo, request.getIngredientiIds());
        processo.setStatoCorrente(StatoProdotto.IN_ATTESA);

        return (Processo) prodottoRepository.save(processo);
    }

    public Tipicita creaTipicita(TipicitaRequest request) {
        Tipicita tipicita = new Tipicita();
        applyCommonProdottoFields(tipicita, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        tipicita.setCertificazioni(request.getCertificazioni());
        if (request.getProcessoId() != null) {
            Prodotto processo = prodottoRepository.findById(request.getProcessoId())
                    .orElseThrow(() -> new IllegalArgumentException("Processo non trovato"));
            if (processo instanceof Processo) {
                tipicita.setProcessoProduttivo((Processo) processo);
            }
        }
        tipicita.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Tipicita) prodottoRepository.save(tipicita);
    }

    public Pacchetto creaPacchetto(PacchettoRequest request) {
        Pacchetto pacchetto = new Pacchetto();
        applyCommonProdottoFields(pacchetto, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        if (request.getTipicitaIds() != null && !request.getTipicitaIds().isEmpty()) {
            List<Prodotto> trovati = prodottoRepository.findAllById(request.getTipicitaIds());
            List<Tipicita> tipicita = trovati.stream()
                    .filter(Tipicita.class::isInstance)
                    .map(Tipicita.class::cast)
                    .collect(Collectors.toList());
            pacchetto.getTipicita().addAll(tipicita);
        }
        pacchetto.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Pacchetto) prodottoRepository.save(pacchetto);
    }

    public Ingrediente modificaIngrediente(Long id, IngredienteRequest request) {
        Ingrediente ingrediente = getProdottoAs(id, Ingrediente.class);
        applyCommonProdottoFields(ingrediente, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        ingrediente.setCertificazioni(request.getCertificazioni());
        ingrediente.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Ingrediente) prodottoRepository.save(ingrediente);
    }

    public Processo modificaProcesso(Long id, ProcessoRequest request) {
        Processo processo = getProdottoAs(id, Processo.class);
        applyCommonProdottoFields(processo, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        processo.setDescrizioneFasi(request.getDescrizioneFasi());
        setIngredientiProcesso(processo, request.getIngredientiIds());
        processo.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Processo) prodottoRepository.save(processo);
    }

    public Tipicita modificaTipicita(Long id, TipicitaRequest request) {
        Tipicita tipicita = getProdottoAs(id, Tipicita.class);
        applyCommonProdottoFields(tipicita, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        tipicita.setCertificazioni(request.getCertificazioni());
        if (request.getProcessoId() != null) {
            Prodotto processo = prodottoRepository.findById(request.getProcessoId())
                    .orElseThrow(() -> new IllegalArgumentException("Processo non trovato"));
            if (processo instanceof Processo) {
                tipicita.setProcessoProduttivo((Processo) processo);
            }
        }
        tipicita.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Tipicita) prodottoRepository.save(tipicita);
    }

    public Pacchetto modificaPacchetto(Long id, PacchettoRequest request) {
        Pacchetto pacchetto = getProdottoAs(id, Pacchetto.class);
        applyCommonProdottoFields(pacchetto, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
        if (request.getTipicitaIds() != null) {
            pacchetto.getTipicita().clear();
            if (!request.getTipicitaIds().isEmpty()) {
                List<Prodotto> trovati = prodottoRepository.findAllById(request.getTipicitaIds());
                List<Tipicita> tipicita = trovati.stream()
                        .filter(Tipicita.class::isInstance)
                        .map(Tipicita.class::cast)
                        .collect(Collectors.toList());
                pacchetto.getTipicita().addAll(tipicita);
            }
        }
        pacchetto.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Pacchetto) prodottoRepository.save(pacchetto);
    }

    public Prodotto aggiornaInventario(Long prodottoId, int quantitaDisponibile, double prezzo) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setQuantitaDisponibile(quantitaDisponibile);
        prodotto.setPrezzo(prezzo);
        return prodottoRepository.save(prodotto);
    }

    public Prodotto approvaProdotto(Long prodottoId) {
        if (prodottoRepository.findByStatoCorrente(StatoProdotto.IN_ATTESA).isEmpty()) {
            throw new IllegalArgumentException("Nessun prodotto in attesa di approvazione");
        }
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setStatoCorrente(StatoProdotto.APPROVATO);
        Prodotto saved = prodottoRepository.save(prodotto);
        notificaEsitoProdotto(saved, StatoProdotto.APPROVATO);
        Catalogo catalogo = getOrCreateCatalogo();
        if (!catalogo.getProdotti().contains(saved)) {
            catalogo.getProdotti().add(saved);
            catalogo.setDataUltimoAggiornamento(LocalDate.now().toString());
            catalogoRepository.save(catalogo);
        }
        return saved;
    }

    public Prodotto respingiProdotto(Long prodottoId, String motivazione) {
        if (prodottoRepository.findByStatoCorrente(StatoProdotto.IN_ATTESA).isEmpty()) {
            throw new IllegalArgumentException("Nessun prodotto in attesa di approvazione");
        }
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setStatoCorrente(StatoProdotto.RESPINTO);
        Prodotto saved = prodottoRepository.save(prodotto);
        notificaEsitoProdotto(saved, StatoProdotto.RESPINTO, motivazione);
        return saved;
    }

    public List<Prodotto> getProdottiApprovati() {
        return prodottoRepository.findByStatoCorrente(StatoProdotto.APPROVATO);
    }

    public List<Prodotto> cercaPerZona(double latitudine, double longitudine, double raggioKm) {
        double raggio = Math.max(0.0, raggioKm);
        return prodottoRepository.findAll().stream()
                .filter(p -> p != null && p.getProduttore() != null && p.getProduttore().getSedi() != null)
                .filter(p -> p.getProduttore().getSedi().stream()
                        .anyMatch(s -> distanzaKm(latitudine, longitudine, s.getLatitudine(), s.getLongitudine()) <= raggio))
                .collect(Collectors.toList());
    }

    public List<Prodotto> getProdottiDiAzienda(Long aziendaId) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        return prodottoRepository.findByProduttore(azienda);
    }

    private double distanzaKm(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }

    private void notificaEsitoProdotto(Prodotto prodotto, StatoProdotto stato) {
        notificaEsitoProdotto(prodotto, stato, null);
    }

    private void notificaEsitoProdotto(Prodotto prodotto, StatoProdotto stato, String motivazione) {
        if (prodotto.getProduttore() == null) {
            return;
        }
        String nomeProdotto = prodotto.getNome() == null ? "Prodotto" : prodotto.getNome();
        String messaggio = nomeProdotto + " è stato " + stato.name().toLowerCase() + ".";
        if (motivazione != null && !motivazione.isBlank()) {
            messaggio += " Motivo: " + motivazione + ".";
        }
        notificaService.inviaNotifica(null, messaggio);
    }

    public List<Prodotto> getProdottiInAttesa() {
        return prodottoRepository.findByStatoCorrente(StatoProdotto.IN_ATTESA);
    }

    public void creaContenuto(Long aziendaId, Long prodottoId, String testo, String urlImmagine) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        List<Prodotto> approvati = prodottoRepository.findByProduttoreAndStatoCorrente(
                azienda, StatoProdotto.APPROVATO);
        if (approvati.isEmpty()) {
            throw new IllegalArgumentException("Azienda senza prodotti approvati");
        }
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        if (prodotto.getProduttore() == null || !azienda.equals(prodotto.getProduttore())) {
            throw new IllegalArgumentException("Prodotto non appartenente all'azienda");
        }
        socialService.pubblicaPost(aziendaId, prodottoId, testo, urlImmagine);
    }

    private void applyCommonProdottoFields(
            Prodotto prodotto,
            String nome,
            String descrizione,
            String provenienza,
            String immagine,
            double prezzo,
            int quantitaDisponibile,
            String dataScadenza,
            Long produttoreId) {
        prodotto.setNome(nome);
        prodotto.setDescrizione(descrizione);
        prodotto.setProvenienza(provenienza);
        prodotto.setImmagine(immagine);
        prodotto.setPrezzo(prezzo);
        prodotto.setQuantitaDisponibile(quantitaDisponibile);
        prodotto.setDataScadenza(dataScadenza);
        if (produttoreId != null) {
            Azienda azienda = aziendaRepository.findById(produttoreId)
                    .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
            prodotto.setProduttore(azienda);
        }
    }

    private <T extends Prodotto> T getProdottoAs(Long id, Class<T> type) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        if (!type.isInstance(prodotto)) {
            throw new IllegalArgumentException("Prodotto non del tipo richiesto");
        }
        return type.cast(prodotto);
    }

    private Catalogo getOrCreateCatalogo() {
        Optional<Catalogo> existing = catalogoRepository.findAll().stream().findFirst();
        if (existing.isPresent()) {
            return existing.get();
        }
        Catalogo catalogo = new Catalogo();
        catalogo.setDataUltimoAggiornamento(LocalDate.now().toString());
        catalogo.setSogliaSpedizioneGratuita(0.0);
        return catalogoRepository.save(catalogo);
    }

    private void setIngredientiProcesso(Processo processo, List<Long> ingredientiIds) {
        if (ingredientiIds == null || ingredientiIds.isEmpty()) {
            throw new IllegalArgumentException("Il processo deve avere almeno un ingrediente");
        }
        List<Prodotto> trovati = prodottoRepository.findAllById(ingredientiIds);
        List<Ingrediente> ingredienti = trovati.stream()
                .filter(Ingrediente.class::isInstance)
                .map(Ingrediente.class::cast)
                .collect(Collectors.toList());
        if (ingredienti.isEmpty()) {
            throw new IllegalArgumentException("Il processo deve avere almeno un ingrediente");
        }
        processo.getIngredientiUsati().clear();
        processo.getIngredientiUsati().addAll(ingredienti);
    }
}
