package bj.eneam.objetsperdus.controller;

import bj.eneam.objetsperdus.entity.*;
import bj.eneam.objetsperdus.service.AnnonceService;
import bj.eneam.objetsperdus.service.MiseEnRelationService;
import bj.eneam.objetsperdus.web.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnnonceController {

    private final AnnonceService annonceService;
    private final MiseEnRelationService miseEnRelationService;

    public AnnonceController(AnnonceService annonceService, MiseEnRelationService miseEnRelationService) {
        this.annonceService = annonceService;
        this.miseEnRelationService = miseEnRelationService;
    }

    /** Accueil = recherche publique des annonces validees. */
    @GetMapping("/")
    public String accueil(@RequestParam(required = false) String motCle,
                          @RequestParam(required = false) Categorie categorie,
                          @RequestParam(required = false) TypeAnnonce type,
                          @RequestParam(required = false) String lieu,
                          Model model) {
        model.addAttribute("annonces", annonceService.rechercher(motCle, categorie, type, lieu));
        model.addAttribute("categories", Categorie.values());
        model.addAttribute("types", TypeAnnonce.values());
        model.addAttribute("motCle", motCle);
        model.addAttribute("categorieFiltre", categorie);
        model.addAttribute("typeFiltre", type);
        model.addAttribute("lieuFiltre", lieu);
        return "accueil";
    }

    @GetMapping("/annonces/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("annonce", annonceService.parId(id));
        return "detail";
    }

    // --- Declaration d'une annonce (utilisateur connecte) ---

    @GetMapping("/annonces/nouvelle")
    public String nouvelleAnnonceForm(HttpSession session, Model model) {
        if (!SessionUtils.estConnecte(session)) {
            return "redirect:/connexion";
        }
        model.addAttribute("annonce", new Annonce());
        model.addAttribute("categories", Categorie.values());
        model.addAttribute("types", TypeAnnonce.values());
        return "annonce-form";
    }

    @PostMapping("/annonces/nouvelle")
    public String creerAnnonce(@Valid @ModelAttribute("annonce") Annonce annonce,
                               BindingResult resultat,
                               HttpSession session,
                               Model model) {
        Utilisateur connecte = SessionUtils.connecte(session);
        if (connecte == null) {
            return "redirect:/connexion";
        }
        if (resultat.hasErrors()) {
            model.addAttribute("categories", Categorie.values());
            model.addAttribute("types", TypeAnnonce.values());
            return "annonce-form";
        }
        annonceService.declarer(annonce, connecte);
        return "redirect:/mes-annonces";
    }

    @GetMapping("/mes-annonces")
    public String mesAnnonces(HttpSession session, Model model) {
        Utilisateur connecte = SessionUtils.connecte(session);
        if (connecte == null) {
            return "redirect:/connexion";
        }
        model.addAttribute("annonces", annonceService.annoncesDe(connecte));
        model.addAttribute("demandes", miseEnRelationService.demandesDe(connecte));
        return "mes-annonces";
    }

    // --- Demande de recuperation (mise en relation via gestionnaire) ---

    @PostMapping("/annonces/{id}/recuperer")
    public String demanderRecuperation(@PathVariable Long id,
                                       @RequestParam String message,
                                       HttpSession session) {
        Utilisateur connecte = SessionUtils.connecte(session);
        if (connecte == null) {
            return "redirect:/connexion";
        }
        miseEnRelationService.demander(id, connecte, message);
        return "redirect:/mes-annonces";
    }
}
