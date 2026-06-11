package bj.eneam.objetsperdus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Column(length = 2000)
    private String description;

    @NotNull(message = "La categorie est obligatoire")
    @Enumerated(EnumType.STRING)
    private Categorie categorie;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    @NotNull(message = "La date est obligatoire")
    private LocalDate dateEvenement;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    private TypeAnnonce type;

    @Enumerated(EnumType.STRING)
    private StatutAnnonce statut = StatutAnnonce.EN_ATTENTE;

    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "auteur_id")
    private Utilisateur auteur;
}
