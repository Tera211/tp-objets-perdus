package bj.eneam.objetsperdus.controller;

import bj.eneam.objetsperdus.entity.Utilisateur;
import bj.eneam.objetsperdus.service.UtilisateurService;
import bj.eneam.objetsperdus.web.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UtilisateurService utilisateurService;

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/connexion")
    public String connexionForm() {
        return "login";
    }

    @PostMapping("/connexion")
    public String connexion(@RequestParam String email,
                            @RequestParam String motDePasse,
                            HttpSession session,
                            Model model) {
        return utilisateurService.authentifier(email, motDePasse)
                .map(u -> {
                    SessionUtils.connecter(session, u);
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    model.addAttribute("erreur", "Email ou mot de passe incorrect");
                    return "login";
                });
    }

    @GetMapping("/inscription")
    public String inscriptionForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "register";
    }

    @PostMapping("/inscription")
    public String inscription(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
                              BindingResult resultat,
                              HttpSession session,
                              Model model) {
        if (resultat.hasErrors()) {
            return "register";
        }
        try {
            Utilisateur cree = utilisateurService.inscrire(utilisateur);
            SessionUtils.connecter(session, cree);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        SessionUtils.deconnecter(session);
        return "redirect:/";
    }
}
