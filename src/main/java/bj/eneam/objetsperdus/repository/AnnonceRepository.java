package bj.eneam.objetsperdus.repository;

import bj.eneam.objetsperdus.entity.Annonce;
import bj.eneam.objetsperdus.entity.Categorie;
import bj.eneam.objetsperdus.entity.StatutAnnonce;
import bj.eneam.objetsperdus.entity.TypeAnnonce;
import bj.eneam.objetsperdus.entity.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    List<Annonce> findByStatutOrderByDateCreationDesc(StatutAnnonce statut);

    List<Annonce> findByAuteurOrderByDateCreationDesc(Utilisateur auteur);

    long countByStatut(StatutAnnonce statut);

    /**
     * Recherche publique multi-criteres : ne renvoie que les annonces VALIDEE.
     * Chaque critere est optionnel (ignore s'il est null).
     */
    @Query("""
            SELECT a FROM Annonce a
            WHERE a.statut = bj.eneam.objetsperdus.entity.StatutAnnonce.VALIDEE
              AND (:motCle IS NULL OR LOWER(a.titre) LIKE LOWER(CONCAT('%', :motCle, '%'))
                   OR LOWER(a.description) LIKE LOWER(CONCAT('%', :motCle, '%')))
              AND (:categorie IS NULL OR a.categorie = :categorie)
              AND (:type IS NULL OR a.type = :type)
              AND (:lieu IS NULL OR LOWER(a.lieu) LIKE LOWER(CONCAT('%', :lieu, '%')))
            ORDER BY a.dateCreation DESC
            """)
    List<Annonce> rechercher(@Param("motCle") String motCle,
                             @Param("categorie") Categorie categorie,
                             @Param("type") TypeAnnonce type,
                             @Param("lieu") String lieu);
}
