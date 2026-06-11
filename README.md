# Plateforme de depot et recherche d'objets perdus

Projet 7 — Application Java EE (Spring Boot + Thymeleaf) permettant de declarer un
objet perdu ou retrouve, de rechercher parmi les annonces, et d'organiser le retrait
des objets via des gestionnaires.

## Lancer avec Docker (recommandé en local — aucun JDK requis)

```bash
docker compose up --build
```

Puis ouvrir : **http://localhost:8020**

> Le port hôte est `8020` (le port 8080 était déjà utilisé sur la machine de dev).
> Pour changer de port, modifier la ligne `ports` dans `docker-compose.yml`.

Pour arrêter : `Ctrl+C` puis `docker compose down`.

## Lancer avec GitHub Codespaces (100% en ligne — zéro installation)

Si vous ne souhaitez pas installer Docker ni Java en local, vous pouvez exécuter le projet gratuitement dans votre navigateur :

1. Importez ce dépôt sur votre compte GitHub.
2. Cliquez sur le bouton vert **Code**, puis sur l'onglet **Codespaces** et cliquez sur **Create codespace on main**.
3. Une fois l'environnement démarré dans votre navigateur, lancez la commande suivante dans le terminal intégré :
   ```bash
   ./mvnw spring-boot:run
   ```
4. Cliquez sur le pop-up ou le lien proposé pour ouvrir l'application web.

## Lancer sans Docker (si JDK 17 installé en local)

```bash
./mvnw spring-boot:run
```


## Comptes de demonstration

| Role | Email | Mot de passe |
|------|-------|--------------|
| Gestionnaire | `gestion@demo.bj` | `1234` |
| Utilisateur | `kevin@demo.bj` | `1234` |
| Utilisateur | `awa@demo.bj` | `1234` |

> La base est H2 en memoire : les donnees sont reinitialisees a chaque demarrage,
> avec un jeu de donnees de demonstration.

## Fonctionnalites

- Inscription / connexion (session HTTP).
- Declaration d'une annonce **PERDU** ou **TROUVE** (statut initial : en attente).
- **Validation avant publication** par un gestionnaire (moderation).
- Recherche publique multi-criteres (mot-cle, categorie, type, lieu) sur les annonces validees.
- **Mise en relation via gestionnaire** : une demande de recuperation passe obligatoirement
  par un gestionnaire (anti-arnaque) ; sa validation cloture l'annonce.

## Console H2

Accessible sur http://localhost:8080/h2-console
(JDBC URL : `jdbc:h2:mem:objetsperdus`, utilisateur : `sa`, sans mot de passe).
