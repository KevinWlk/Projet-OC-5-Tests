import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect, jest } from '@jest/globals';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './services/session.service';
import { SessionInformation } from './interfaces/sessionInformation.interface';
import { BehaviorSubject } from 'rxjs';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule,
      ],
      declarations: [
        AppComponent,
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  describe("Logout", () => {

    let mockAuthService: any = jest.fn(); //créer une fonction mockée pour simuler les services
    let mockRouter: any = { navigate: jest.fn() }; //Simule la méthode de navigation du routeur Angular
    let mockSessionService: any = { logOut: jest.fn() }; // Simule la méthode de déco
//Crée une instance de AppComponent en passant les services mockés.
    let mockComponent: AppComponent = new AppComponent(
      mockAuthService as AuthService,
      mockRouter,
      mockSessionService as SessionService,
    );

    it("Logout successfully", () => {
      let isLogged: boolean = true;
      let sessionInformation: SessionInformation | undefined;
      let isLoggedSubject = new BehaviorSubject<boolean>(isLogged);

      // Appeler la méthode logout
      mockComponent.logout();

      // Vérifie que la méthode logOut de session.service.ts a été appelée une fois
      expect(mockSessionService.logOut).toHaveBeenCalledTimes(1);

      // Mock de la méthode logOut avec des données actualisées
      mockSessionService.logOut.mockImplementation(() => {
        sessionInformation = undefined;
        isLogged = false;
        isLoggedSubject.next(isLogged);
      });

      // Vérifie que la navigation a été appelée une fois
      expect(mockRouter.navigate).toHaveBeenCalledTimes(1);

      // Vérifie que la navigation redirige correctement
      expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
    });
  });
});
