import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { throwError } from "rxjs";
import { AuthService } from "../../services/auth.service";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    // Configure le module de test
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Vérifie que le composant est créé
    expect(component).toBeTruthy();
  });

  it('Should form value be of type LoginRequest', () => {
    // Remplit le formulaire avec des valeurs de test
    const expectedEmail = 'test@test.com';
    const expectedPassword = 'password';
    component.form.controls['email'].setValue(expectedEmail);
    component.form.controls['password'].setValue(expectedPassword);

    // Soumet le formulaire
    component.submit();

    // Vérifie que les valeurs du formulaire sont correctes
    const loginRequest = component.form.value;
    expect(loginRequest).toBeTruthy();
    expect(loginRequest.email).toEqual(expectedEmail);
    expect(loginRequest.password).toEqual(expectedPassword);
  });

  it('Should set onError to true when login fails', () => {
    // Mock AuthService pour simuler une erreur de login
    const authService = TestBed.inject(AuthService);
    jest.spyOn(authService, 'login').mockReturnValue(throwError('error'));

    // Soumet le formulaire pour tester l'erreur
    component.submit();

    // Vérifie que onError est bien à true
    expect(component.onError).toBe(true);
  });

});
