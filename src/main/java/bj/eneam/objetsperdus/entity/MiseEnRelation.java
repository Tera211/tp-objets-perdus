package bj.eneam.objetsperdus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Demande de recuperation d'un objet. Elle materialise le passage obligatoire
 * par un gestionnaire : le demandeur ne contacte jamais directement l'auteur,
 * c'est le gestionnaire qui valide la mise en relation (anti-arnaque).
 */
@Getter
@Setter
@Entity
@Table(name = "mises_en_relation")
public class MiseEnRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "annonce_id")
    private Annonce annonce;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demandeur_id")
    private Utilisateur demandeur;

    @ManyToOne
    @JoinColumn(name = "gestionnaire_id")
    private Utilisateur gestionnaire;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private StatutRelation statut = StatutRelation.EN_COURS;

    private LocalDateTime dateCreation = LocalDateTime.now();
}
