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

    public GestoreCatalogoService(
            ProdottoRepository prodottoRepository,
            AziendaRepository aziendaRepository,
            CatalogoRepository catalogoRepository,
            ISocialService socialService) {
        this.prodottoRepository = prodottoRepository;
        this.aziendaRepository = aziendaRepository;
        this.catalogoRepository = catalogoRepository;
        this.socialService = socialService;
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
        processo.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Processo) prodottoRepository.save(processo);
    }

    public Tipicita creaTipicita(TipicitaRequest request) {
        Tipicita tipicita = new Tipicita();
        applyCommonProdottoFields(tipicita, request.getNome(), request.getDescrizione(),
                request.getProvenienza(), request.getImmagine(), request.getPrezzo(),
                request.getQuantitaDisponibile(), request.getDataScadenza(), request.getProduttoreId());
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
        pacchetto.setContenuto(request.getContenuto());
        pacchetto.setStatoCorrente(StatoProdotto.IN_ATTESA);
        return (Pacchetto) prodottoRepository.save(pacchetto);
    }

    public Prodotto modificaProdotto(Long prodottoId, String nome, String descrizione, double prezzo) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setNome(nome);
        prodotto.setDescrizione(descrizione);
        prodotto.setPrezzo(prezzo);
        return prodottoRepository.save(prodotto);
    }

    public Prodotto aggiornaInventario(Long prodottoId, int quantitaDisponibile) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setQuantitaDisponibile(quantitaDisponibile);
        return prodottoRepository.save(prodotto);
    }

    public Prodotto approvaProdotto(Long prodottoId) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setStatoCorrente(StatoProdotto.APPROVATO);
        Prodotto saved = prodottoRepository.save(prodotto);
        Catalogo catalogo = getOrCreateCatalogo();
        if (!catalogo.getProdotti().contains(saved)) {
            catalogo.getProdotti().add(saved);
            catalogo.setDataUltimoAggiornamento(LocalDate.now().toString());
            catalogoRepository.save(catalogo);
        }
        return saved;
    }

    public Prodotto respingiProdotto(Long prodottoId) {
        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));
        prodotto.setStatoCorrente(StatoProdotto.RESPINTO);
        return prodottoRepository.save(prodotto);
    }

    public List<Prodotto> getProdottiApprovati() {
        return prodottoRepository.findByStatoCorrente(StatoProdotto.APPROVATO);
    }

    public List<Prodotto> cercaPerZona(String zona) {
        return prodottoRepository.findAll().stream()
                .filter(p -> p.getProvenienza() != null && p.getProvenienza().toLowerCase().contains(zona.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Prodotto> getProdottiDiAzienda(Long aziendaId) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new IllegalArgumentException("Azienda non trovata"));
        return prodottoRepository.findByProduttore(azienda);
    }

    public List<Prodotto> getProdottiInAttesa() {
        return prodottoRepository.findByStatoCorrente(StatoProdotto.IN_ATTESA);
    }

    public void pubblicaProdottoSuSocial(Long aziendaId, Long prodottoId, String testo) {
        socialService.pubblicaPost(aziendaId, prodottoId, testo);
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
}
