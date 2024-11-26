import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { SessionApiService } from "../features/sessions/services/session-api.service";
import { Teacher } from "../interfaces/teacher.interface";
import { of } from "rxjs";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClientMock: any;
  let teacherMock: Teacher;

  beforeEach(() => {
    // Mock HttpClient avec une méthode get
    httpClientMock = { get: jest.fn() };

    // Configuration du module de test
    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        { provide: HttpClient, useValue: httpClientMock },
      ],
      imports: [HttpClientModule]
    });

    service = TestBed.inject(TeacherService);

    // Mock Teacher pour les tests
    teacherMock = {
      id: 1,
      lastName: 'TestLastname',
      firstName: 'TestFirstName',
      createdAt: new Date(),
      updatedAt: new Date()
    };
  });

  it('should be created', () => {
    // Vérifie que le service est créé
    expect(service).toBeTruthy();
  });

  it('should return an Observable<Teacher[]> for all', () => {
    // Mock d'un tableau de Teacher pour all()
    const teachersMock: Teacher[] = [teacherMock];
    httpClientMock.get.mockReturnValue(of(teachersMock));

    // Vérifie que all() retourne les enseignants attendus
    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(teachersMock);
    });
  });

  it('should return an Observable<Teacher> for detail', () => {
    // Mock d'un Teacher pour detail()
    httpClientMock.get.mockReturnValue(of(teacherMock));

    // Vérifie que detail() retourne l'enseignant attendu
    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(teacherMock);
    });
  });
});
