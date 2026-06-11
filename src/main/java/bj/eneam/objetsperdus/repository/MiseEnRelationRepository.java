package bj.eneam.objetsperdus.repository;

import bj.eneam.objetsperdus.entity.MiseEnRelation;
import bj.eneam.objetsperdus.entity.StatutRelation;
import bj.eneam.objetsperdus.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiseEnRelationRepository extends JpaRepository<MiseEnRelation, Long> {

    List<MiseEnRelation> findByStatutOrderByDateCreationDesc(StatutRelation statut);

    List<MiseEnRelation> findByDemandeurOrderByDateCreationDesc(Utilisateur demandeur);

    long countByStatut(StatutRelation statut);
}
