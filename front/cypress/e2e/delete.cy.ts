/// <reference types="cypress" />

describe("Supprimer une session", () => {
  it("Supprime une session existante", () => {
    // Mock de connexion
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: "harry.potter@poudlard.fr",
        firstName: 'Harry',
        lastName: 'Potter',
        admin: true
      }
    });

    // Liste des sessions mockées
    let sessions = [
      {
        id: 1,
        name: "Défense contre les forces du mal",
        description: "Cours avec le professeur Lupin",
        date: "2023-01-15 09:00:00",
        teacher_id: 1,
        users: [],
        createdAt: "2023-09-08 18:45:03",
        updatedAt: "2023-09-12 23:23:22",
      },
      {
        id: 2,
        name: "Potions",
        description: "Cours avec le professeur Rogue",
        date: "2023-01-16 10:00:00",
        teacher_id: 2,
        users: [],
        createdAt: "2023-09-12 23:13:47",
        updatedAt: "2023-09-12 23:13:47",
      }
    ];

    // Mock de la liste des sessions
    cy.intercept("GET", '/api/session', {
      body: sessions,
    });

    // Liste des enseignants mockés
    const enseignants = [
      {
        id: 1,
        lastName: "Lupin",
        firstName: "Rémus",
        createdAt: "2023-08-29 18:57:01",
        updatedAt: "2023-08-29 18:57:01",
      },
      {
        id: 2,
        lastName: "Rogue",
        firstName: "Severus",
        createdAt: "2023-08-29 18:57:01",
        updatedAt: "2023-08-29 18:57:01",
      },
    ];

    // Mock de la liste des enseignants
    cy.intercept('GET', '/api/teacher', enseignants);

    // Connexion
    cy.get('input[formControlName=email]').type("harry.potter@poudlard.fr");
    cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

    cy.get('.mat-raised-button').should("be.enabled");
    cy.url().should('include', '/sessions');

    // Session à supprimer
    let session = {
      id: 1,
      name: "Défense contre les forces du mal",
      description: "Cours avec le professeur Lupin",
      date: "2023-01-15 09:00:00",
      teacher_id: 1,
      users: [],
      createdAt: "2023-09-08 18:45:03",
      updatedAt: "2023-09-12 23:23:22",
    };

    const enseignant = enseignants.find((teacher) => teacher.id == session.teacher_id);

    // Mock des détails de la session et de l'enseignant
    cy.intercept("GET", `/api/session/${session.id}`, session);
    cy.intercept("GET", `/api/teacher/${session.teacher_id}`, enseignant);

    // Navigation vers les détails de la session
    cy.contains('Detail').click();
    cy.url().should("include", `/sessions/detail/${session.id}`);

    // Mock de la suppression
    cy.intercept("DELETE", `/api/session/${session.id}`, {});

    // Mise à jour de la liste des sessions après suppression
    const sessionsList = sessions.filter(s => s.id !== session.id);
    cy.intercept("GET", "/api/session", {
      body: sessionsList,
    });

    // Suppression de la session
    cy.contains("Delete").click();

    // Vérification du retour à la liste des sessions
    cy.url().should("include", "/sessions");
    cy.contains(session.name).should('not.exist');
  });
});
