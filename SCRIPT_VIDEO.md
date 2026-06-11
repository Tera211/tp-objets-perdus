# Script de la vidéo de démonstration — Projet 7

**Durée cible : 3 à 5 minutes.** La vidéo doit montrer le fonctionnement réel de
l'application. Préparer l'écran : `docker compose up --build` puis ouvrir
`http://localhost:8020`. Avoir deux fenêtres/onglets (ou navigation privée) pour jouer
l'utilisateur et le gestionnaire séparément.

---

### 0:00 — Introduction (≈ 20 s)
> « Bonjour, nous présentons le Projet 7 : une plateforme de dépôt et de recherche
> d'objets perdus, développée en Java EE avec Spring Boot. Elle permet de déclarer un
> objet, de le rechercher, et organise la récupération de façon sécurisée. »

Montrer la page d'accueil avec les annonces déjà publiées.

### 0:20 — La recherche publique (≈ 30 s)
- Saisir un mot-clé (ex. « téléphone »).
- Utiliser les filtres : catégorie, type (Perdu/Trouvé), lieu.
> « Seules les annonces validées par un gestionnaire apparaissent ici. »

### 0:50 — Déclarer un objet (utilisateur) (≈ 50 s)
- Se connecter avec `kevin@demo.bj` / `1234`.
- Menu **Déclarer** → remplir le formulaire (type, titre, catégorie, lieu, date, description).
- Valider.
> « L'annonce est créée mais reste *en attente* : elle n'est pas encore visible du public. »
- Ouvrir **Mes annonces** → montrer le statut **EN_ATTENTE**.

### 1:40 — Validation par le gestionnaire (≈ 50 s)
- Se déconnecter, se reconnecter avec `gestion@demo.bj` / `1234`.
- Menu **Modération** → la nouvelle annonce apparaît dans la file.
> « Le gestionnaire contrôle chaque annonce avant publication. »
- Cliquer **Valider**.
- Revenir sur l'accueil → l'annonce est maintenant **publique et recherchable**.

### 2:30 — Demande de récupération (anti-arnaque) (≈ 50 s)
- Se reconnecter en utilisateur (`awa@demo.bj` / `1234`).
- Ouvrir une annonce → encart **Récupérer cet objet**.
> « Point important : l'utilisateur ne contacte jamais directement l'auteur. »
- Écrire un message de preuve → **Envoyer la demande**.
- Montrer dans **Mes annonces** la demande au statut **EN_COURS**.

### 3:20 — Traitement par le gestionnaire (≈ 40 s)
- Se reconnecter en gestionnaire → menu **Relations**.
- Montrer la demande, le message et le contact du demandeur.
> « Le gestionnaire sert d'intermédiaire de confiance pour organiser le retrait. »
- Cliquer **Valider** → l'annonce passe **CLÔTURÉE** et disparaît des résultats publics.

### 4:00 — Architecture & déploiement (≈ 30 s)
- Montrer brièvement le code : packages `entity / repository / service / controller`.
- Montrer le `Dockerfile` / `docker-compose.yml`.
> « L'application est en couches Spring Boot et se lance en une seule commande Docker,
> sans installer Java. »

### 4:30 — Conclusion (≈ 20 s)
> « Notre plateforme couvre toutes les exigences : déclaration et recherche d'objets,
> annonces perdus/retrouvés, validation avant publication, et mise en relation encadrée
> par les gestionnaires pour limiter les arnaques. Merci. »

---

**Conseils d'enregistrement**
- Outil : QuickTime (Mac) ou OBS Studio. Enregistrer écran + voix.
- Zoomer le navigateur (Cmd +) pour la lisibilité.
- Couper les temps morts au montage ; viser ≤ 5 min.
