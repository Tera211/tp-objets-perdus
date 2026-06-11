package bj.eneam.objetsperdus.controller;

import bj.eneam.objetsperdus.entity.Utilisateur;
import bj.eneam.objetsperdus.service.AnnonceService;
import bj.eneam.objetsperdus.service.MiseEnRelationService;
import bj.eneam.objetsperdus.web.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Espace reserve aux gestionnaires : moderation des annonces et gestion
 * des mises en relation (retrait des objets).
 */
@Controller
@RequestMapping("/gestion")
public class GestionController {

    private final AnnonceService annonceService;
    private final MiseEnRelationService miseEnRelationService;

    public GestionController(AnnonceService annonceService, MiseEnRelationService miseEnRelationService) {
        this.annonceService = annonceService;
        this.miseEnRelationService = miseEnRelationService;
    }

    // --- File de moderation des annonces ---

    @GetMapping("/moderation")
    public String moderation(HttpSession session, Model model) {
        if (!SessionUtils.estGestionnaire(session)) {
            return "redirect:/";
        }
        model.addAttribute("annonces", annonceService.annoncesEnAttente());
        return "gestion/moderation";
    }

    @PostMapping("/annonces/{id}/valider")
    public String validerAnnonce(@PathVariable Long id, HttpSession session) {
        if (!SessionUtils.estGestionnaire(session)) {
            return "redirect:/";
        }
        annonceService.valider(id);
        return "redirect:/gestion/moderation";
    }

    @PostMapping("/annonces/{id}/rejeter")
    public String rejeterAnnonce(@PathVariable Long id, HttpSession session) {
        if (!SessionUtils.estGestionnaire(session)) {
            return "redirect:/";
        }
        annonceService.rejeter(id);
        return "redirect:/gestion/moderation";
    }

    // --- Gestion des mises en relation ---

    @GetMapping("/relations")
    public String relations(HttpSession session, Model model) {
        if (!SessionUtils.estGestionnaire(session)) {
            return "redirect:/";
        }
        model.addAttribute("relations", miseEnRelationService.enCours());
        return "gestion/relations";
    }

    @PostMapping("/relations/{id}/valider")
    public String validerRelation(@PathVariable Long id, HttpSession session) {
        Utilisateur gestionnaire = SessionUtils.connecte(session);
        if (gestionnaire == null || !SessionUtils.estGestionnaire(session)) {
            return "redirect:/";
        }
        miseEnRelationService.valider(id, gestionnaire);
        return "redirect:/gestion/relations";
    }

    @PostMapping("/relations/{id}/refuser")
    public String refuserRelation(@PathVariable Long id, HttpSession session) {
        Utilisateur gestionnaire = SessionUtils.connecte(session);
        if (gestionnaire == null || !SessionUtils.estGestionnaire(session)) {
            return "redirect:/";
        }
        miseEnRelationService.refuser(id, gestionnaire);
        return "redirect:/gestion/relations";
    }
}
