
# Testez une application full-stack

## Spécifications techniques

### Stack Technique
- **Front-end** : Angular
- **Back-end** : Java Spring Boot
- **Base de données** : MySQL

---

## Cloner le projet

```bash
git clone https://github.com/KevinWlk/Projet-OC-5-Tests.git
```

---

## Lancer la partie Front

1. Se rendre dans le dossier front :
   ```bash
   cd front
   ```

2. Installer les dépendances :
   ```bash
   npm install
   ```

3. Lancer le serveur :
   ```bash
   npm run start
   ```

---

## Lancer la partie Back

1. Se rendre dans le dossier back :
   ```bash
   cd back
   ```

2. Installer les dépendances :
   ```bash
   mvn clean install
   ```

3. Créer la base de données :
   ```bash
   mysql -u root -p
   ```
   Ensuite, exécuter les commandes suivantes dans le terminal MySQL :
   ```sql
   CREATE DATABASE test;
   ```

4. Lancer le script SQL pour initialiser la base de données :
   - Fichier : `ressources/sql/script.sql`

5. Lancer le serveur :
   ```bash
   mvn spring-boot:run
   ```

---

## Lancer les tests sur la partie Front

1. Se rendre dans le dossier front :
   ```bash
   cd front
   ```

2. Installer les dépendances :
   ```bash
   npm install
   ```

3. Lancer les tests :
   - **Technologies utilisées** : Jest & Cypress

   - Lancer les tests avec Jest :
     ```bash
     npm run test
     ```
     Pour obtenir un rapport de couverture des tests :
     ```bash
     npm run test:coverage
     ```

   - Lancer les tests avec Cypress :
     ```bash
     npm run e2e
     ```
     Pour obtenir un rapport de couverture des tests :
     ```bash
     npm run e2e:coverage
     ```

---

## Lancer les tests sur la partie Back

1. Se rendre dans le dossier back :
   ```bash
   cd back
   ```

2. Installer les dépendances :
   ```bash
   mvn clean install
   ```

3. Lancer le script SQL pour initialiser les données de test :
   - Fichier : `ressources/sql/script.sql`

4. Lancer les tests :
   - **Technologies utilisées** : JUnit, Mockito, Jacoco

   ```bash
   mvn test
   ```

5. Générer un rapport de couverture avec Jacoco :
   ```bash
   mvn jacoco:report
   ```
