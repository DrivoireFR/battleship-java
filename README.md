# Terminal Battleship

## Concept et objectif

Terminal Battleship est un mini-projet Maven/Spring Boot conçu pour pratiquer une **démarche qualité simplifiée** à
travers le développement d'un jeu "touché/coulé" en ligne de commande.

L'objectif pédagogique est de :

- Implémenter quelques **User Stories** en suivant les bonnes pratiques de développement
- Appliquer une démarche qualité avec tests unitaires, couverture de code et détection de duplication
- Expérimenter le TDD (Test-Driven Development) et la revue de code

Le projet suit une approche qualité avec :

- Tests unitaires pour chaque fonctionnalité
- Couverture de code minimale de 50% par classe (hors classes d'infrastructure)
- Détection de duplication de code via PMD/CPD
- Validation automatique des seuils de qualité lors du build

## Prérequis

- **Java 17** ou supérieur
- **Maven 3.6+** (ou Maven wrapper)

## Lancer le projet en local

### Compilation et exécution

```bash
# Compiler le projet
mvn clean compile

# Lancer l'application
mvn spring-boot:run

# Lancer l'application en mode silencieux
mvn spring-boot:run -Dspring-boot.run.arguments="--quiet"
```

### Utilisation

Une fois lancé, le jeu vous demande de saisir des coordonnées au format `lettre + chiffre` (ex: `e5`, `A3`). Tapez
`quit` pour arrêter la partie.

## Tests et rapports de qualité

### Exécuter les tests

```bash
# Exécuter uniquement les tests unitaires
mvn test

# Exécuter les tests + vérifications qualité (couverture, duplication)
mvn verify
```

### Rapports générés

Après l'exécution de `mvn verify`, les rapports sont disponibles dans le répertoire `target/` :

#### 📊 Rapport de couverture de code (JaCoCo)

**Emplacement :** `target/site/jacoco/index.html`

Ouvrez ce fichier dans un navigateur pour visualiser :

- La couverture globale du projet
- La couverture par package et par classe
- Les lignes couvertes (vertes) et non couvertes (rouges)

**Seuil configuré :** Minimum 50% de couverture par classe (classes d'infrastructure exclues)

#### 🔍 Rapport de duplication de code (CPD)

**Emplacement :** `target/reports/cpd.html`

Affiche les blocs de code dupliqués détectés dans le projet.

**Seuil configuré :** Minimum 75 tokens pour détecter une duplication

#### 📝 Rapports de tests (Surefire)

**Emplacement :** `target/surefire-reports/`

Contient les résultats détaillés des tests :

- `TEST-*.xml` : Rapports XML pour intégration CI/CD
- `*.txt` : Rapports texte lisibles

### Commandes utiles

```bash
# Nettoyer et reconstruire
mvn clean install

# Exécuter les tests avec affichage détaillé
mvn test -X

# Ignorer temporairement les vérifications de couverture
mvn verify -Djacoco.skip=true

# Générer uniquement le rapport de couverture (sans vérification)
mvn jacoco:report
```

## Structure du projet

```
terminal-battleship/
├── src/
│   ├── main/java/
│   │   └── com/qualityworkshop/terminalbattleship/
│   │       ├── board/           # Modèle de grille et coordonnées
│   │       ├── cli/             # Interface ligne de commande
│   │       ├── game/            # Moteur de jeu
│   │       ├── rendering/       # Rendu ASCII des grilles
│   │       └── strategy/        # Stratégies de tir IA
│   └── test/java/               # Tests unitaires
├── target/
│   ├── site/jacoco/             # Rapports JaCoCo
│   ├── reports/cpd.html         # Rapport de duplication
│   └── surefire-reports/        # Rapports de tests
└── pom.xml                      # Configuration Maven
```

## Technologies utilisées

- **Java 17**
- **Spring Boot 3.2.5**
- **JUnit 5** (via spring-boot-starter-test)
- **JaCoCo** : Mesure de couverture de code
- **PMD/CPD** : Détection de duplication de code

## Seuils de qualité

- **Couverture de code** : Minimum 50% par classe (hors infrastructure)
- **Duplication** : Détection de blocs ≥ 75 tokens
- **Tests** : Tous les tests doivent passer

Ces seuils sont vérifiés automatiquement si vous faite un `mvn verify`.

