# Mini-rapport — Projet 7

## Plateforme de dépôt et recherche d'objets perdus

**Cours** : Programmation Java EE
**Membres du groupe** : _(à compléter — 5 maximum)_
**Date** : juin 2026

---

## 1. Contexte et objectifs

De nombreuses personnes perdent chaque jour des objets (téléphones, documents, clés…)
et d'autres en retrouvent sans savoir comment joindre le propriétaire. L'objectif du
projet est de développer une **application Java EE** centralisant :

- la **déclaration** d'un objet perdu et le lancement de sa recherche ;
- les **annonces** d'objets perdus **et** retrouvés ;
- la **mise en relation** sécurisée entre le déclarant et celui qui détient l'objet.

Deux contraintes fortes du sujet structurent l'application :

1. **Toute annonce de perte doit être validée avant publication** → une étape de
   modération par un gestionnaire.
2. **Toute mise en relation pour le retrait passe par les gestionnaires** de la
   plateforme, afin de **limiter les risques d'arnaque** → le demandeur ne contacte
   jamais directement l'auteur.

---

## 2. Fonctionnalités

### Côté utilisateur
- Création de compte et connexion (session).
- Déclaration d'un objet **perdu** ou **trouvé** (titre, catégorie, lieu, date, description).
- Consultation de ses propres annonces et de leur statut (en attente / validée / rejetée / clôturée).
- Recherche publique multi-critères (mot-clé, catégorie, type, lieu) sur les annonces validées.
- Demande de récupération d'un objet (transmise à un gestionnaire).

### Côté gestionnaire
- **Modération** : file des annonces en attente, validation ou rejet.
- **Mises en relation** : file des demandes de récupération, validation (qui clôture
  l'annonce) ou refus.

---

## 3. Modèle de données

```
+----------------+         +------------------+         +----------------------+
|  Utilisateur   |         |     Annonce      |         |   MiseEnRelation     |
+----------------+         +------------------+         +----------------------+
| id             |1       *| id               |1       *| id                   |
| nom            |---------| titre            |---------| annonce  (FK)        |
| prenom         | auteur  | description      | annonce | demandeur (FK Util.) |
| email (unique) |         | categorie (enum) |         | gestionnaire (FK)    |
| motDePasse     |         | lieu             |         | message              |
| telephone      |         | dateEvenement    |         | statut (enum)        |
| role (enum)    |         | type (enum)      |         | dateCreation         |
+----------------+         | statut (enum)    |         +----------------------+
                           | dateCreation     |
                           +------------------+

Enums :
- Role          : UTILISATEUR | GESTIONNAIRE
- TypeAnnonce   : PERDU | TROUVE
- StatutAnnonce : EN_ATTENTE | VALIDEE | REJETEE | CLOTUREE
- Categorie     : TELEPHONE | DOCUMENTS | CLES | SAC | VETEMENT | BIJOU | AUTRE
- StatutRelation: EN_COURS | VALIDEE | REFUSEE
```

L'entité **MiseEnRelation** est le cœur de la règle anti-arnaque : elle relie une
annonce, le **demandeur** et le **gestionnaire** qui traite la demande.

---

## 4. Architecture technique

Architecture en couches classique Spring Boot (MVC) :

```
Navigateur ──HTTP──> Controller ──> Service ──> Repository ──> Base H2
                         │
                         └──> Vue Thymeleaf (HTML)
```

| Couche | Rôle | Classes |
|--------|------|---------|
| `entity` | Modèle persistant (JPA) | Utilisateur, Annonce, MiseEnRelation + enums |
| `repository` | Accès aux données (Spring Data JPA) | UtilisateurRepository, AnnonceRepository, MiseEnRelationRepository |
| `service` | Logique métier | UtilisateurService, AnnonceService, MiseEnRelationService |
| `controller` | Points d'entrée web | AuthController, AnnonceController, GestionController |
| `web` | Session & sécurité légère | SessionUtils, GlobalModelAdvice |
| `config` | Données de démo | DataLoader |
| `templates` | Vues | Thymeleaf (accueil, détail, formulaires, espace gestion) |

### Choix techniques
- **Spring Boot 3.5 / Java 17** (aligné sur le cours).
- **Thymeleaf** pour les vues côté serveur.
- **Spring Data JPA** + **base H2 en mémoire** (zéro installation, idéale pour la démo).
- **Bean Validation** pour le contrôle des formulaires.
- **Authentification par session HTTP** (rôles UTILISATEUR / GESTIONNAIRE), sans
  framework de sécurité externe — adaptée au périmètre du TP.
- **Docker** (Dockerfile multi-stage + docker-compose) pour un lancement sans
  installer Java.

---

## 5. Règles métier clés (mapping avec le sujet)

| Exigence du sujet | Implémentation |
|-------------------|----------------|
| Déclarer un objet perdu / lancer sa recherche | `AnnonceController` + page de recherche `accueil.html` |
| Annonces d'objets perdus **et** retrouvés | champ `TypeAnnonce { PERDU, TROUVE }` |
| **Validation avant publication** | annonce créée `EN_ATTENTE` ; seule une annonce `VALIDEE` est visible publiquement (`AnnonceRepository.rechercher`) ; modération par le gestionnaire |
| **Mise en relation via gestionnaires (anti-arnaque)** | entité `MiseEnRelation` créée `EN_COURS` ; validation/refus réservés au gestionnaire ; la validation clôture l'annonce |

---

## 6. Mode d'emploi

### Lancement (Docker, recommandé en local)
```bash
docker compose up --build
# puis http://localhost:8020
```

### Lancement (GitHub Codespaces, 100% en ligne)
Si vous ne voulez rien installer sur votre PC :
1. Créez un dépôt GitHub et poussez le projet dessus.
2. Ouvrez le projet dans GitHub Codespaces.
3. Dans le terminal intégré, lancez :
   ```bash
   ./mvnw spring-boot:run
   ```

### Lancement (Sans Docker, avec Java 17 en local)
```bash
./mvnw spring-boot:run
# puis http://localhost:8080
```


### Comptes de démonstration
| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Gestionnaire | gestion@demo.bj | 1234 |
| Utilisateur | kevin@demo.bj | 1234 |
| Utilisateur | awa@demo.bj | 1234 |

### Scénario de démonstration
1. Se connecter en **utilisateur**, déclarer un objet → l'annonce est *en attente*.
2. Se connecter en **gestionnaire** → onglet *Modération* → **valider** l'annonce.
3. Revenir sur l'accueil : l'annonce est désormais publique et **recherchable**.
4. En utilisateur, ouvrir une annonce → **demander la récupération** (message).
5. En gestionnaire → onglet *Relations* → **valider** : l'annonce passe *clôturée*.

---

## 7. Captures d'écran

_(insérer ici les captures)_

- Figure 1 — Page de recherche / accueil
- Figure 2 — Formulaire de déclaration
- Figure 3 — File de modération (gestionnaire)
- Figure 4 — Demande de récupération
- Figure 5 — Gestion des mises en relation

---

## 8. Limites et perspectives

- Mots de passe stockés en clair (le périmètre du TP exclut Spring Security) →
  perspective : hachage **BCrypt** + Spring Security.
- Base **H2 en mémoire** réinitialisée à chaque démarrage → perspective : MySQL/PostgreSQL persistant.
- Ajouts possibles : upload de photos des objets, notifications email, messagerie
  interne tracée, tableau de bord statistique pour les gestionnaires.

---

## 9. Conclusion

L'application couvre l'intégralité des exigences du Projet 7 : déclaration et recherche
d'objets, annonces d'objets perdus et retrouvés, **validation avant publication** et
**mise en relation encadrée par des gestionnaires** pour limiter les arnaques. Elle est
construite sur une architecture Spring Boot en couches, claire et extensible, et se
lance en une commande grâce à Docker.
