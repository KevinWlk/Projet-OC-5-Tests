import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    // Configure le module de test
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    // Vérifie que le service est créé
    expect(service).toBeTruthy();
  });

  it('should be initialized with isLogged as false', () => {
    // Vérifie que isLogged est initialisé à false
    expect(service.isLogged).toBe(false);
  });

  it('should set isLogged to true and update sessionInformation on logIn', () => {
    // Mock de session pour le test
    const mockSessionInformation: SessionInformation = {
      token: 'string',
      type: 'string',
      id: 1,
      username: 'TestUsername',
      firstName: 'TestFirstName',
      lastName: 'TestLastname',
      admin: false
    };

    // Teste l’effet de logIn()
    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInformation);
  });

  it('should set isLogged to false and reset sessionInformation on logOut', () => {
    // Teste l’effet de logOut()
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should return an Observable that emits the correct value of isLogged', (done) => {
    // Vérifie que $isLogged() émet la valeur correcte
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(service.isLogged);
      done();
    });
  });
});
