# Trame de présentation (PPT) — Projet 7

> Trame prête à copier dans PowerPoint / Google Slides. Une section `##` = une diapo.
> Le texte sous chaque titre = les puces de la diapo.

---

## Diapo 1 — Page de titre
- **Projet 7 : Plateforme de dépôt et recherche d'objets perdus**
- Cours de Programmation Java EE
- Membres du groupe : _(noms — 5 max)_
- Juin 2026

## Diapo 2 — Contexte & problématique
- Beaucoup d'objets perdus / retrouvés chaque jour
- Pas de canal fiable pour relier propriétaire et personne qui détient l'objet
- Risque d'arnaque lors des « récupérations »
- → Besoin d'une plateforme centralisée et **sécurisée**

## Diapo 3 — Objectifs de l'application
- Déclarer un objet perdu et lancer sa recherche
- Publier des annonces d'objets **perdus** et **retrouvés**
- Valider les annonces **avant** publication
- Encadrer la mise en relation par des **gestionnaires**

## Diapo 4 — Acteurs et rôles
- **Utilisateur** : déclare, recherche, demande une récupération
- **Gestionnaire** : modère les annonces, valide les mises en relation
- (Schéma : 2 personnages → la plateforme)

## Diapo 5 — Fonctionnalités principales
- Inscription / connexion
- Déclaration d'annonce (perdu / trouvé)
- Recherche multi-critères (mot-clé, catégorie, type, lieu)
- Espace gestionnaire : modération + relations

## Diapo 6 — Règle 1 : validation avant publication
- Toute annonce naît au statut **EN ATTENTE**
- Invisible du public tant qu'elle n'est pas **VALIDÉE**
- Le gestionnaire valide ou rejette depuis sa file de modération
- (Capture : file de modération)

## Diapo 7 — Règle 2 : mise en relation anti-arnaque
- Le demandeur **ne contacte jamais directement** l'auteur
- Il dépose une demande → traitée par un **gestionnaire**
- Le gestionnaire valide (→ annonce **clôturée**) ou refuse
- (Capture : gestion des relations)

## Diapo 8 — Architecture technique
- Spring Boot 3.5 / Java 17 / Thymeleaf
- Couches : Controller → Service → Repository → Base H2
- Authentification par session (rôles)
- (Schéma en couches)

## Diapo 9 — Modèle de données
- Utilisateur, Annonce, MiseEnRelation
- Enums : Role, TypeAnnonce, StatutAnnonce, Catégorie, StatutRelation
- (Diagramme entités/relations)

## Diapo 10 — Déploiement
- Conteneurisé avec **Docker** (Dockerfile multi-stage + docker-compose)
- Lancement en une commande : `docker compose up --build`
- Aucune installation Java requise → http://localhost:8020

## Diapo 11 — Démonstration
- Déclaration → Modération → Recherche → Récupération → Clôture
- (Renvoi vers la vidéo)

## Diapo 12 — Limites & perspectives
- Mots de passe en clair → BCrypt + Spring Security
- H2 en mémoire → base persistante (MySQL/PostgreSQL)
- Photos des objets, notifications email, statistiques

## Diapo 13 — Conclusion
- Toutes les exigences du Projet 7 couvertes
- Architecture claire, sécurisée par la modération
- Démarrage simple grâce à Docker

## Diapo 14 — Merci
- Questions ?
- Contact du groupe
