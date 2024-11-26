/// <reference types="cypress" />

import { getMonthName } from "../services/monthFormat";

describe("List spec", () => {
  it("Shows list of sessions for admin", () => {
    // Login mock
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: "yoga@studio.com",
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      }
    });

    const sessions = [
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
    ];

    cy.intercept("GET", '/api/session', {
      body: sessions,
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

    cy.get('.mat-raised-button').should("be.enabled");
    cy.url().should('include', '/sessions');

    // Show all sessions informations for admin
    cy.contains("Rentals available").should("be.visible");
    cy.contains("Create").should("be.visible");
    sessions.forEach((session) => {
      cy.contains(session.name).should('be.visible');
      cy.contains(session.description).should('be.visible');
      const picture = cy.get('img.picture');
      picture.should('have.attr', 'src', 'assets/sessions.png');
      picture.should('have.attr', 'alt', 'Yoga session');
      const year = session.date.slice(0, 10).split('-')[0];
      const month = getMonthName(parseInt(session.date.slice(0, 10).split('-')[1]));
      const day = parseInt(session.date.slice(0, 10).split('-')[2]);
      const date = `Session on ${month} ${day}, ${year}`;
      cy.contains(date).should('be.visible');
      cy.contains("Detail").should("be.visible");
      cy.contains("Edit").should("be.visible");
    });
  });

  it("Shows list of sessions for non admin users", () => {
    // Login mock
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 4,
        username: "toto3@toto.com",
        firstName: 'toto',
        lastName: 'toto',
        admin: false
      }
    });

    const sessions = [
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
    ];

    cy.intercept("GET", '/api/session', {
      body: sessions,
    });

    cy.get('input[formControlName=email]').type("toto3@toto.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('.mat-raised-button').should("be.enabled");
    cy.url().should('include', '/sessions');

    // Show all sessions informations for non admin users
    cy.contains("Rentals available").should("be.visible");
    cy.contains("Create").should("not.exist");
    sessions.forEach((session) => {
      cy.contains(session.name).should('be.visible');
      cy.contains(session.description).should('be.visible');
      const picture = cy.get('img.picture');
      picture.should('have.attr', 'src', 'assets/sessions.png');
      picture.should('have.attr', 'alt', 'Yoga session');
      const year = session.date.slice(0, 10).split('-')[0];
      const month = getMonthName(parseInt(session.date.slice(0, 10).split('-')[1]));
      const day = parseInt(session.date.slice(0, 10).split('-')[2]);
      const date = `Session on ${month} ${day}, ${year}`;
      cy.contains(date).should('be.visible');
      cy.contains("Detail").should("be.visible");
      cy.contains("Edit").should("not.exist");
    });
  });
});
