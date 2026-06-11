package bj.eneam.objetsperdus.web;

import bj.eneam.objetsperdus.entity.Utilisateur;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Expose l'utilisateur connecte a toutes les vues Thymeleaf (barre de navigation).
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("connecte")
    public Utilisateur connecte(HttpSession session) {
        return SessionUtils.connecte(session);
    }
}
