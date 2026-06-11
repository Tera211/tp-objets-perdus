package bj.eneam.objetsperdus.service;

import bj.eneam.objetsperdus.entity.Role;
import bj.eneam.objetsperdus.entity.Utilisateur;
import bj.eneam.objetsperdus.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repository;

    public UtilisateurService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    /** Inscription d'un nouvel utilisateur (role UTILISATEUR par defaut). */
    public Utilisateur inscrire(Utilisateur utilisateur) {
        if (repository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Un compte existe deja avec cet email");
        }
        utilisateur.setRole(Role.UTILISATEUR);
        return repository.save(utilisateur);
    }

    /** Authentification simple par email + mot de passe. */
    public Optional<Utilisateur> authentifier(String email, String motDePasse) {
        return repository.findByEmail(email)
                .filter(u -> u.getMotDePasse().equals(motDePasse));
    }

    public Utilisateur enregistrer(Utilisateur utilisateur) {
        return repository.save(utilisateur);
    }
}
