/// <reference types="cypress" />

describe("Detail spec", () => {
  it("Participate to session", () => {
    // Login mock
    cy.visit('/login');

    const user = {
      id: 4,
      username: "jean.dupont@example.com",
      firstName: 'Jean',
      lastName: 'Dupont',
      admin: false
    }
    cy.intercept('POST', '/api/auth/login', {
      body: user
    });

    let sessions = [
      {
        id: 1,
        name: "Atelier Yoga Débutant",
        description: "Séance pour découvrir les bases du yoga",
        date: "2024-11-15 10:00:00",
        teacher_id: 1,
        users: [],
        createdAt: "2024-11-01 09:00:00",
        updatedAt: "2024-11-05 12:00:00",
      },
      {
        id: 2,
        name: "Méditation Guidée",
        description: "Atelier pour explorer la pleine conscience",
        date: "2024-11-20 15:00:00",
        teacher_id: 2,
        users: [],
        createdAt: "2024-11-01 09:00:00",
        updatedAt: "2024-11-01 09:00:00",
      }
    ]

    cy.intercept("GET", '/api/session', {
      body: sessions,
    });

    const teachers = [
      {
        id: 1,
        lastName: "Lemoine",
        firstName: "Claire",
        createdAt: "2024-11-01 08:00:00",
        updatedAt: "2024-11-01 08:00:00",
      },
      {
        id: 2,
        lastName: "Morel",
        firstName: "Jean",
        createdAt: "2024-11-01 08:00:00",
        updatedAt: "2024-11-01 08:00:00",
      },
    ]

    cy.intercept('GET', '/api/teacher', teachers);

    cy.get('input[formControlName=email]').type("jean.dupont@example.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('.mat-raised-button').should("be.enabled");
    cy.url().should('include', '/sessions');

    let session = {
      id: 1,
      name: "Atelier Yoga Débutant",
      description: "Séance pour découvrir les bases du yoga",
      date: "2024-11-15 10:00:00",
      teacher_id: 1,
      users: [],
      createdAt: "2024-11-01 09:00:00",
      updatedAt: "2024-11-05 12:00:00",
    }

    const teacher = teachers.find((teacher) => teacher.id == session.teacher_id);

    cy.intercept("GET", "/api/session/1", {
      body:session
    });

    cy.intercept("GET", `/api/teacher/${session.id}`, teacher)

    const sessionCard =  cy.get('.items > :nth-child(1)');
    sessionCard.contains('Detail').click();
    cy.contains('Participate').should("be.visible");

    cy.intercept("POST", `/api/session/1/participate/${user.id}`, {})
    cy.intercept("GET", "/api/session/1", {
      id: 1,
      name: "Atelier Yoga Débutant",
      description: "Séance pour découvrir les bases du yoga",
      date: "2024-11-15 10:00:00",
      teacher_id: 1,
      users: [user.id],
      createdAt: "2024-11-01 09:00:00",
      updatedAt: "2024-11-05 12:00:00",
    })
    cy.contains("Participate").click();


  })

  it("Cancel participation", () => {
    // Login mock
    cy.visit('/login');

    const user = {
      id: 4,
      username: "jean.dupont@example.com",
      firstName: 'Jean',
      lastName: 'Dupont',
      admin: false
    }
    cy.intercept('POST', '/api/auth/login', {
      body: user
    });

    let sessions = [
      {
        id: 1,
        name: "Atelier Yoga Débutant",
        description: "Séance pour découvrir les bases du yoga",
        date: "2024-11-15 10:00:00",
        teacher_id: 1,
        users: [],
        createdAt: "2024-11-01 09:00:00",
        updatedAt: "2024-11-05 12:00:00",
      },
      {
        id: 2,
        name: "Méditation Guidée",
        description: "Atelier pour explorer la pleine conscience",
        date: "2024-11-20 15:00:00",
        teacher_id: 2,
        users: [],
        createdAt: "2024-11-01 09:00:00",
        updatedAt: "2024-11-01 09:00:00",
      }
    ]

    cy.intercept("GET", '/api/session', {
      body: sessions,
    });

    const teachers = [
      {
        id: 1,
        lastName: "Lemoine",
        firstName: "Claire",
        createdAt: "2024-11-01 08:00:00",
        updatedAt: "2024-11-01 08:00:00",
      },
      {
        id: 2,
        lastName: "Morel",
        firstName: "Jean",
        createdAt: "2024-11-01 08:00:00",
        updatedAt: "2024-11-01 08:00:00",
      },
    ]

    cy.intercept('GET', '/api/teacher', teachers);

    cy.get('input[formControlName=email]').type("jean.dupont@example.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('.mat-raised-button').should("be.enabled");
    cy.url().should('include', '/sessions');

    let session = {
      id: 1,
      name: "Atelier Yoga Débutant",
      description: "Séance pour découvrir les bases du yoga",
      date: "2024-11-15 10:00:00",
      teacher_id: 1,
      users: [user.id],
      createdAt: "2024-11-01 09:00:00",
      updatedAt: "2024-11-05 12:00:00",
    }

    const teacher = teachers.find((teacher) => teacher.id == session.teacher_id);

    cy.intercept("GET", "/api/session/1", {
      body:session
    });

    cy.intercept("GET", `/api/teacher/${session.id}`, teacher)

    const sessionCard =  cy.get('.items > :nth-child(1)');
    sessionCard.contains('Detail').click();

    cy.intercept("DELETE", `/api/session/1/participate/${user.id}`, {});
    cy.intercept("GET", "/api/session/1", {
      id: 1,
      name: "Atelier Yoga Débutant",
      description: "Séance pour découvrir les bases du yoga",
      date: "2024-11-15 10:00:00",
      teacher_id: 1,
      users: [],
      createdAt: "2024-11-01 09:00:00",
      updatedAt: "2024-11-05 12:00:00",
    })
    // Do not participate button
    const doNotParticipateButton =  cy.contains('Do not participate');
    doNotParticipateButton.should("be.visible");
    doNotParticipateButton.click();
  })
})
