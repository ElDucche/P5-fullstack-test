# 🧘‍♀️ Application Yoga - Tests et Code Coverage

Cette application Yoga est composée d'un backend Spring Boot et d'un frontend Angular. Ce README explique comment exécuter les tests unitaires et d'intégration avec le code coverage pour les deux parties du projet.

## 📋 Prérequis

### Outils requis
- **Java 11+** - Pour le backend Spring Boot
- **Node.js 14+** et **npm** - Pour le frontend Angular
- **Maven 3.6+** - Pour la gestion des dépendances backend
- **Git** - Pour cloner le projet

### Vérification des versions
```bash
java -version
node --version
npm --version
mvn --version
```

## 🚀 Installation et Configuration

### 1. Cloner le projet
```bash
git clone <url-du-projet>
cd P5-fullstack-test
```

### 2. Configuration de la base de données (Backend)
Le backend utilise H2 en mémoire pour les tests, aucune configuration supplémentaire n'est nécessaire.

### 3. Installation des dépendances

#### Backend
```bash
cd back
mvn clean install
```

#### Frontend
```bash
cd front
npm install
```

## 🧪 Exécution des Tests

### Backend (Spring Boot + Maven)

#### Lancer tous les tests avec code coverage
```bash
cd back
mvn clean test
```

#### Lancer les tests avec génération du rapport de couverture
```bash
cd back
mvn clean test jacoco:report
```

#### Lancer uniquement les tests unitaires
```bash
cd back
mvn test -Dtest="!**/*Integration*"
```

#### Lancer uniquement les tests d'intégration
```bash
cd back
mvn test -Dtest="**/*Integration*"
```

#### Résultats et rapports backend
- **Tests** : Résultats dans `back/target/surefire-reports/`
- **Code coverage** : Rapport HTML dans `back/target/site/jacoco/index.html`
- **Fichier coverage** : `back/target/jacoco.exec`

### Frontend (Angular + Jest)

#### Lancer tous les tests avec code coverage
```bash
cd front
npm run test
```

#### Lancer les tests en mode watch
```bash
cd front
npm run test:watch
```

#### Lancer les tests avec couverture de code détaillée
```bash
cd front
npm run test:coverage
```

#### Résultats et rapports frontend
- **Tests** : Résultats affichés dans le terminal
- **Code coverage** : Rapport HTML dans `front/coverage/lcov-report/index.html`
- **Fichiers coverage** : 
  - `front/coverage/lcov.info`
  - `front/coverage/coverage-final.json`

## 📊 Tests End-to-End (E2E)

### Cypress (Frontend)

#### Lancer les tests E2E en mode interactif
```bash
cd front
npm run e2e
```

#### Lancer les tests E2E en mode headless
```bash
cd front
npm run e2e:ci
```

#### Code coverage E2E
```bash
cd front
npm run e2e:coverage
```

## 📈 Rapports de Couverture

### Visualiser les rapports

#### Backend (JaCoCo)
```bash
# Après avoir lancé les tests
open back/target/site/jacoco/index.html
# ou naviguer vers file:///.../back/target/site/jacoco/index.html
```

#### Frontend (Jest)
```bash
# Après avoir lancé les tests avec coverage
open front/coverage/lcov-report/index.html
# ou naviguer vers file:///.../front/coverage/lcov-report/index.html
```

#### E2E (Cypress)
```bash
# Après avoir lancé les tests E2E avec coverage
open front/coverage/lcov-report/index.html
```

### Objectifs de couverture

- **Backend** : Minimum 80% de couverture de code
- **Frontend** : Minimum 80% de couverture de code
- **E2E** : Couverture des parcours utilisateur principaux

## 🛠️ Commandes Utiles

### Backend
```bash
# Nettoyer et recompiler
mvn clean compile

# Lancer l'application en mode développement
mvn spring-boot:run

# Générer uniquement le rapport de couverture
mvn jacoco:report

# Lancer un test spécifique
mvn test -Dtest=SessionControllerTest
```

### Frontend
```bash
# Lancer l'application en mode développement
npm start

# Lancer les tests d'un composant spécifique
npm test -- --testNamePattern="LoginComponent"

# Lancer les tests avec mise à jour des snapshots
npm test -- --updateSnapshot

# Build de production
npm run build
```

## 🐛 Résolution de Problèmes

### Problèmes Backend
- **Erreur de compilation MapStruct** : Exécuter `mvn clean compile`
- **Tests qui échouent** : Vérifier que H2 est bien configuré
- **Problème Spring Security** : Vérifier la configuration `WebSecurityConfig`

### Problèmes Frontend
- **Erreurs de dépendances** : Supprimer `node_modules` et relancer `npm install`
- **Tests qui échouent** : Vérifier la configuration Jest dans `jest.config.js`
- **Problèmes E2E** : S'assurer que l'application backend est démarrée

### Cache et nettoyage
```bash
# Backend
cd back
mvn clean

# Frontend
cd front
rm -rf node_modules package-lock.json
npm install
npm run test -- --clearCache
```

## 📁 Structure des Tests

### Backend (`back/src/test/`)
```
test/
├── java/
│   └── com/openclassrooms/starterjwt/
│       ├── controllers/        # Tests des contrôleurs
│       ├── services/          # Tests des services
│       ├── repository/        # Tests d'intégration
│       ├── mapper/           # Tests des mappers
│       ├── security/         # Tests de sécurité
│       └── dto/              # Tests des DTOs
└── resources/
    └── application.properties # Configuration de test
```

### Frontend (`front/src/`)
```
src/
├── app/
│   ├── components/           # Tests des composants
│   ├── services/            # Tests des services
│   └── guards/              # Tests des guards
├── cypress/
│   ├── e2e/                # Tests E2E
│   ├── fixtures/           # Données de test
│   └── support/            # Utilitaires Cypress
└── test-config.helper.ts    # Configuration des tests
```

## 🎯 Bonnes Pratiques

1. **Toujours lancer les tests avant de pusher** le code
2. **Maintenir un minimum de 80% de couverture** de code
3. **Écrire des tests pour chaque nouvelle fonctionnalité**
4. **Utiliser des mocks appropriés** pour isoler les tests
5. **Nommer les tests de manière descriptive**

## 📞 Support

En cas de problème avec les tests :
1. Vérifier les prérequis et versions
2. Consulter les logs d'erreur détaillés
3. Nettoyer les caches et dépendances
4. Vérifier la configuration des outils de test