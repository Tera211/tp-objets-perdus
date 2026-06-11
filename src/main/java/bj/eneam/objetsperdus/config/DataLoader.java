package bj.eneam.objetsperdus.config;

import bj.eneam.objetsperdus.entity.*;
import bj.eneam.objetsperdus.repository.AnnonceRepository;
import bj.eneam.objetsperdus.repository.MiseEnRelationRepository;
import bj.eneam.objetsperdus.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Jeu de donnees injecte au demarrage pour rendre la demo immediate.
 * Comptes : gestion@demo.bj / 1234 (gestionnaire), kevin@demo.bj & awa@demo.bj / 1234.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final AnnonceRepository annonceRepository;
    private final MiseEnRelationRepository miseEnRelationRepository;

    public DataLoader(UtilisateurRepository utilisateurRepository,
                      AnnonceRepository annonceRepository,
                      MiseEnRelationRepository miseEnRelationRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.annonceRepository = annonceRepository;
        this.miseEnRelationRepository = miseEnRelationRepository;
    }

    @Override
    public void run(String... args) {
        if (utilisateurRepository.count() > 0) {
            return;
        }

        Utilisateur gestionnaire = creerUtilisateur("Sagbo", "Romaric", "gestion@demo.bj", "1234",
                "+229 90 00 00 00", Role.GESTIONNAIRE);
        Utilisateur kevin = creerUtilisateur("Dossou", "Kevin", "kevin@demo.bj", "1234",
                "+229 91 11 11 11", Role.UTILISATEUR);
        Utilisateur awa = creerUtilisateur("Bello", "Awa", "awa@demo.bj", "1234",
                "+229 92 22 22 22", Role.UTILISATEUR);

        // Annonces validees (visibles publiquement)
        creerAnnonce("Telephone Samsung noir", "Smartphone Samsung Galaxy avec coque bleue, perdu pres du marche.",
                Categorie.TELEPHONE, "Marche Dantokpa, Cotonou", LocalDate.now().minusDays(3),
                TypeAnnonce.PERDU, StatutAnnonce.VALIDEE, kevin);
        creerAnnonce("Trousseau de cles trouve", "Trousseau de 4 cles avec porte-cle rouge trouve devant la fac.",
                Categorie.CLES, "Campus ENEAM, Cotonou", LocalDate.now().minusDays(2),
                TypeAnnonce.TROUVE, StatutAnnonce.VALIDEE, awa);
        creerAnnonce("Carte d'identite au nom de J. Houngbedji", "CNI trouvee dans un taxi, ramenee au point d'accueil.",
                Categorie.DOCUMENTS, "Gare de Cotonou", LocalDate.now().minusDays(1),
                TypeAnnonce.TROUVE, StatutAnnonce.VALIDEE, awa);

        // Annonces en attente de validation (file de moderation)
        creerAnnonce("Sac a dos noir perdu", "Sac a dos contenant des cahiers et une calculatrice.",
                Categorie.SAC, "Arret de bus, Calavi", LocalDate.now().minusDays(1),
                TypeAnnonce.PERDU, StatutAnnonce.EN_ATTENTE, kevin);
        creerAnnonce("Montre trouvee", "Montre argentee trouvee sur un banc.",
                Categorie.BIJOU, "Place de l'Etoile Rouge", LocalDate.now(),
                TypeAnnonce.TROUVE, StatutAnnonce.EN_ATTENTE, awa);

        // Une demande de recuperation en cours sur la 1ere annonce validee
        Annonce telephone = annonceRepository.findByStatutOrderByDateCreationDesc(StatutAnnonce.VALIDEE)
                .stream().filter(a -> a.getType() == TypeAnnonce.PERDU).findFirst().orElse(null);
        if (telephone != null) {
            MiseEnRelation relation = new MiseEnRelation();
            relation.setAnnonce(telephone);
            relation.setDemandeur(awa);
            relation.setMessage("Je pense avoir retrouve ce telephone, je peux decrire la coque.");
            relation.setStatut(StatutRelation.EN_COURS);
            miseEnRelationRepository.save(relation);
        }
    }

    private Utilisateur creerUtilisateur(String nom, String prenom, String email, String mdp,
                                         String tel, Role role) {
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setMotDePasse(mdp);
        u.setTelephone(tel);
        u.setRole(role);
        return utilisateurRepository.save(u);
    }

    private void creerAnnonce(String titre, String description, Categorie categorie, String lieu,
                              LocalDate date, TypeAnnonce type, StatutAnnonce statut, Utilisateur auteur) {
        Annonce a = new Annonce();
        a.setTitre(titre);
        a.setDescription(description);
        a.setCategorie(categorie);
        a.setLieu(lieu);
        a.setDateEvenement(date);
        a.setType(type);
        a.setStatut(statut);
        a.setAuteur(auteur);
        annonceRepository.save(a);
    }
}
