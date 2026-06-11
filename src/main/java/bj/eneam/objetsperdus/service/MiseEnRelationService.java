package bj.eneam.objetsperdus.service;

import bj.eneam.objetsperdus.entity.*;
import bj.eneam.objetsperdus.repository.MiseEnRelationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiseEnRelationService {

    private final MiseEnRelationRepository repository;
    private final AnnonceService annonceService;

    public MiseEnRelationService(MiseEnRelationRepository repository, AnnonceService annonceService) {
        this.repository = repository;
        this.annonceService = annonceService;
    }

    /**
     * Un utilisateur qui reconnait un objet depose une demande de recuperation.
     * Elle part EN_COURS : seul un gestionnaire pourra la valider.
     */
    public MiseEnRelation demander(Long annonceId, Utilisateur demandeur, String message) {
        Annonce annonce = annonceService.parId(annonceId);
        MiseEnRelation relation = new MiseEnRelation();
        relation.setAnnonce(annonce);
        relation.setDemandeur(demandeur);
        relation.setMessage(message);
        relation.setStatut(StatutRelation.EN_COURS);
        return repository.save(relation);
    }

    public List<MiseEnRelation> enCours() {
        return repository.findByStatutOrderByDateCreationDesc(StatutRelation.EN_COURS);
    }

    public List<MiseEnRelation> demandesDe(Utilisateur demandeur) {
        return repository.findByDemandeurOrderByDateCreationDesc(demandeur);
    }

    public long nombreEnCours() {
        return repository.countByStatut(StatutRelation.EN_COURS);
    }

    public MiseEnRelation parId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));
    }

    /**
     * Le gestionnaire valide la mise en relation : il prend en charge le retrait
     * et l'annonce concernee est cloturee.
     */
    public void valider(Long id, Utilisateur gestionnaire) {
        MiseEnRelation relation = parId(id);
        relation.setGestionnaire(gestionnaire);
        relation.setStatut(StatutRelation.VALIDEE);
        repository.save(relation);
        annonceService.cloturer(relation.getAnnonce().getId());
    }

    public void refuser(Long id, Utilisateur gestionnaire) {
        MiseEnRelation relation = parId(id);
        relation.setGestionnaire(gestionnaire);
        relation.setStatut(StatutRelation.REFUSEE);
        repository.save(relation);
    }
}
