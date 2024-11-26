import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from "../interfaces/session.interface";
import { of } from "rxjs";

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClientMock: any;
  let sessionMock: Session;

  beforeEach(() => {
    // Mock HttpClient avec les méthodes HTTP
    httpClientMock = {
      get: jest.fn(),
      delete: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
    };

    // Configure le module de test pour injecter le service
    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        { provide: HttpClient, useValue: httpClientMock },
      ],
      imports: [
        HttpClientModule
      ]
    });

    service = TestBed.inject(SessionApiService);

    // Mock de données pour une session
    sessionMock = { id: 1, name: 'Test Session 1', description: 'Test Description 1', date: new Date(), teacher_id: 1, users: [1, 2] };
  });

  it('should be created', () => {
    // Vérifie que le service est créé avec succès
    expect(service).toBeTruthy();
  });

  it('should return an Observable<Session[]> for all', () => {
    // Mock la méthode get pour retourner un tableau de sessions
    const sessionsMock: Session[] = [sessionMock];
    httpClientMock.get.mockReturnValue(of(sessionsMock));

    // Vérifie que all() retourne les sessions simulées
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(sessionsMock);
    });
  });

  it('should return an Observable<Session> for detail', () => {
    // Mock la méthode get pour retourner une session unique
    httpClientMock.get.mockReturnValue(of(sessionMock));

    // Vérifie que detail() retourne la session simulée
    service.detail('1').subscribe((session) => {
      expect(session).toEqual(sessionMock);
    });
  });

  it('should return an Observable<any> for delete', () => {
    // Mock la méthode delete pour retourner un Observable null
    httpClientMock.delete.mockReturnValue(of(null));

    // Vérifie que delete() émet une réponse définie (null attendu ici)
    service.delete('1').subscribe((response) => {
      expect(response).toBeDefined();
    });
  });

  it('should return an Observable<Session> for create', () => {
    // Mock la méthode post pour retourner la session créée
    httpClientMock.post.mockReturnValue(of(sessionMock));

    // Vérifie que create() retourne la session simulée
    service.create(sessionMock).subscribe((session) => {
      expect(session).toEqual(sessionMock);
    });
  });

  it('should return an Observable<Session> for update', () => {
    // Mock la méthode put pour retourner la session mise à jour
    httpClientMock.put.mockReturnValue(of(sessionMock));

    // Vérifie que update() retourne la session simulée
    service.update('1', sessionMock).subscribe((session) => {
      expect(session).toEqual(sessionMock);
    });
  });

  it('should return an Observable<void> for participate', () => {
    // Mock la méthode post pour participer à une session
    httpClientMock.post.mockReturnValue(of(null));

    // Vérifie que participate() retourne une réponse définie (null attendu ici)
    service.participate('1', '1').subscribe((response) => {
      expect(response).toBeDefined();
    });
  });

  it('should return an Observable<void> for unParticipate', () => {
    // Mock la méthode delete pour se désinscrire d'une session
    httpClientMock.delete.mockReturnValue(of(null));

    // Vérifie que unParticipate() retourne une réponse définie (null attendu ici)
    service.unParticipate('1', '1').subscribe((response) => {
      expect(response).toBeDefined();
    });
  });
});
