/// <reference types="cypress" />

import { checkEmailFormat } from "../services/emailFormat";
import { fieldValidator } from "../services/fieldValidator";

describe('Test d’enregistrement', () => {

  // Cas où l'enregistrement est réussi
  it('Enregistrement réussi', () => {
    // Visite de la page d'inscription
    cy.visit('/register');

    // Interception de la requête POST pour l'inscription
    cy.intercept('POST', '/api/auth/register', {
      body: {
        firstName: 'Jean',
        lastName: 'Dupont',
        email: 'jean.dupont@email.com',
        password: 'motdepasse123'
      },
    });

    // Interception de la requête GET pour les sessions (initialement vide)
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    // Données valides pour l'inscription
    const prenom: string = "Jean";
    const nom: string = "Dupont";
    const email: string = "jean.dupont@email.com";
    const motDePasse: string = "motdepasse123";

    // Remplissage des champs du formulaire
    cy.get('input[formControlName=firstName]').type(prenom);
    cy.get('input[formControlName=lastName]').type(nom);
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(motDePasse);

    // Validation des champs
    if (
      fieldValidator(prenom, 3, 20) &&
      fieldValidator(nom, 3, 20) &&
      checkEmailFormat(email) &&
      fieldValidator(motDePasse, 3, 40)
    ) {
      // Vérifie que le bouton est activé et le soumet
      cy.get('.register-form > .mat-focus-indicator').should("be.enabled");
      cy.get('.register-form > .mat-focus-indicator').click();

      // Vérifie la redirection vers la page de connexion
      cy.url().should('include', '/login');
    } else {
      // Si les champs sont invalides, le bouton doit être désactivé
      cy.get('.register-form > .mat-focus-indicator').should("be.disabled");
      cy.url().should('not.include', '/login');
      cy.contains("Une erreur est survenue").should("be.visible");
    }
  });

  it("Register failed, invalid fields", () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: "Bad request",
      statusCode: 400,
    })

    const firstName : string = "b";
    const lastName : string = "12";
    const email : string = "test@bad.a";
    const password : string = "11";

    cy.get('input[formControlName=firstName]').type(firstName);
    cy.get('input[formControlName=lastName]').type(lastName);
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);

    if(
      fieldValidator(firstName,3,20) &&
      fieldValidator(lastName,3,20) &&
      checkEmailFormat(email) &&
      fieldValidator(password,3,40)
    ){
      cy.get('.register-form > .mat-focus-indicator').should("be.enabled");
      cy.get('.register-form > .mat-focus-indicator').click()
      cy.url().should('include', '/login');
    }else{
      cy.get('.register-form > .mat-focus-indicator').click();
      cy.url().should('not.include', '/login');
      cy.contains("An error occurred").should("be.visible");
    }
  })
})
