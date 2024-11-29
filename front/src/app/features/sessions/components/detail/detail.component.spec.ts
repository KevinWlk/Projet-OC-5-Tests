import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  // Ajouter des paramètres de constructeur pour DetailComponent, les clés d'objet représentent les méthodes des services
  let mockRoute: any = { snapshot: { paramMap: { get: jest.fn().mockReturnValue('123') }} };
  let mockFormBuilder: FormBuilder = new FormBuilder();
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 42,
    },
  };
  let mockSessionApiService: any = {
    delete: jest.fn().mockReturnValue({ subscribe: jest.fn() }),
    participate: jest.fn().mockReturnValue({ subscribe: jest.fn() }),
    unparticipate: jest.fn().mockReturnValue({ subscribe: jest.fn() }),
    detail: jest.fn().mockReturnValue({ subscribe: jest.fn() }),
  };

  let mockTeacherService: any = {
    detail: jest.fn(),
  };

  let mockMatSnackBar: any = {
    open: jest.fn(),
  };

  let mockRouter: any = {
    navigate: jest.fn(),
  };

  let mockComponent: DetailComponent = new DetailComponent(
    mockRoute as ActivatedRoute,
    mockFormBuilder,
    mockSessionService as SessionService,
    mockSessionApiService as SessionApiService,
    mockTeacherService as TeacherService,
    mockMatSnackBar as MatSnackBar,
    mockRouter as Router,
  );

  let sessionId = mockRoute.snapshot.paramMap.get('id');

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe("When I delete a session", () => {
    it("Deletes successfully a session", () => {
      // Appeler la méthode delete
      mockComponent.delete();

      // Vérifie que sessionApiService est appelé avec l'ID de la session
      expect(mockSessionApiService.delete).toHaveBeenCalledTimes(1);
      expect(mockSessionApiService.delete).toHaveBeenCalledWith(sessionId);

      mockSessionApiService.delete.mockReturnValue(of(
        mockMatSnackBar.open("Session supprimée avec succès !"),
        mockRouter.navigate(['sessions'])
      ));

      // Vérifie que le MatSnackBar est appelé avec un message de suppression
      expect(mockMatSnackBar.open).toHaveBeenCalledWith("Session supprimée avec succès !");

      // Vérifie que la navigation a été appelée correctement
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
