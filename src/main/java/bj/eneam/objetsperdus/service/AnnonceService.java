package bj.eneam.objetsperdus.service;

import bj.eneam.objetsperdus.entity.*;
import bj.eneam.objetsperdus.repository.AnnonceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceService {

    private final AnnonceRepository repository;

    public AnnonceService(AnnonceRepository repository) {
        this.repository = repository;
    }

    /** Declaration d'une annonce : toujours creee EN_ATTENTE de validation. */
    public Annonce declarer(Annonce annonce, Utilisateur auteur) {
        annonce.setAuteur(auteur);
        annonce.setStatut(StatutAnnonce.EN_ATTENTE);
        return repository.save(annonce);
    }

    /** Recherche publique : uniquement les annonces validees. */
    public List<Annonce> rechercher(String motCle, Categorie categorie, TypeAnnonce type, String lieu) {
        return repository.rechercher(vide(motCle), categorie, type, vide(lieu));
    }

    public List<Annonce> annoncesEnAttente() {
        return repository.findByStatutOrderByDateCreationDesc(StatutAnnonce.EN_ATTENTE);
    }

    public List<Annonce> annoncesDe(Utilisateur auteur) {
        return repository.findByAuteurOrderByDateCreationDesc(auteur);
    }

    public Annonce parId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));
    }

    public long nombreEnAttente() {
        return repository.countByStatut(StatutAnnonce.EN_ATTENTE);
    }

    // --- Moderation par le gestionnaire ---

    public void valider(Long id) {
        Annonce a = parId(id);
        a.setStatut(StatutAnnonce.VALIDEE);
        repository.save(a);
    }

    public void rejeter(Long id) {
        Annonce a = parId(id);
        a.setStatut(StatutAnnonce.REJETEE);
        repository.save(a);
    }

    public void cloturer(Long id) {
        Annonce a = parId(id);
        a.setStatut(StatutAnnonce.CLOTUREE);
        repository.save(a);
    }

    private String vide(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
