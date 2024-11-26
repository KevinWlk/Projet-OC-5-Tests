import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from "../../services/auth.service";
import { throwError } from "rxjs";
import { RegisterRequest } from "../../interfaces/registerRequest.interface";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    // Configure le module de test
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Vérifie que le composant est créé
    expect(component).toBeTruthy();
  });

  it('Should form value be of type RegisterRequest', () => {
    // Définit les valeurs de test pour le formulaire
    const expectedEmail = 'test@test.com';
    const expectedFirstName = 'firstName';
    const expectedLastName = 'lastName';
    const expectedPassword = 'password';

    // Remplit le formulaire avec les valeurs de test
    component.form.controls['email'].setValue(expectedEmail);
    component.form.controls['firstName'].setValue(expectedFirstName);
    component.form.controls['lastName'].setValue(expectedLastName);
    component.form.controls['password'].setValue(expectedPassword);

    // Soumet le formulaire
    component.submit();

    // Vérifie que la valeur du formulaire correspond à RegisterRequest
    const registerRequest = component.form.value;
    expect(registerRequest).toEqual({
      email: expectedEmail,
      firstName: expectedFirstName,
      lastName: expectedLastName,
      password: expectedPassword
    });
  });

  it('Should set onError to true when register fails', () => {
    // Mock AuthService pour simuler une erreur d'enregistrement
    const authService = TestBed.inject(AuthService);
    jest.spyOn(authService, 'register').mockReturnValue(throwError('Error'));

    // Soumet le formulaire pour tester l'erreur
    component.submit();

    // Vérifie que onError est bien à true
    expect(component.onError).toBe(true);
  });

});
