package bj.eneam.objetsperdus.web;

import bj.eneam.objetsperdus.entity.Role;
import bj.eneam.objetsperdus.entity.Utilisateur;
import jakarta.servlet.http.HttpSession;

/**
 * Petite aide pour gerer l'utilisateur connecte en session HTTP
 * (authentification simple, sans Spring Security).
 */
public final class SessionUtils {

    public static final String CLE_UTILISATEUR = "utilisateurConnecte";

    private SessionUtils() {
    }

    public static Utilisateur connecte(HttpSession session) {
        return (Utilisateur) session.getAttribute(CLE_UTILISATEUR);
    }

    public static boolean estConnecte(HttpSession session) {
        return connecte(session) != null;
    }

    public static boolean estGestionnaire(HttpSession session) {
        Utilisateur u = connecte(session);
        return u != null && u.getRole() == Role.GESTIONNAIRE;
    }

    public static void connecter(HttpSession session, Utilisateur utilisateur) {
        session.setAttribute(CLE_UTILISATEUR, utilisateur);
    }

    public static void deconnecter(HttpSession session) {
        session.invalidate();
    }
}
