import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { User } from "../../interfaces/user.interface";
import { UserService } from "../../services/user.service";
import { of } from "rxjs";
import { Router } from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  // Mock services
  const mockSessionService = {
    sessionInformation: { admin: true, id: 1 },
    logOut: jest.fn()
  };

  const mockUserService = {
    delete: jest.fn().mockReturnValue(of({})),
    getById: jest.fn().mockReturnValue(of({
      id: 1,
      email: 'test@test.com',
      lastName: 'Test',
      firstName: 'User',
      admin: true,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    }))
  };

  const mockMatSnackBar = { open: jest.fn() };
  const mockRouter = { navigate: jest.fn() };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Vérifie que le composant est créé
    expect(component).toBeTruthy();
  });

  it('Should call userService.getById with correct id on ngOnInit', () => {
    // Mock utilisateur
    const userService = TestBed.inject(UserService);
    const mockUser: User = {
      id: 1,
      email: 'test@test.com',
      lastName: 'Test',
      firstName: 'User',
      admin: true,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    const spy = jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));

    // Test ngOnInit
    component.ngOnInit();
    expect(spy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(component.user).toEqual(mockUser);
  });

  it('Should call history.back on back', () => {
    // Test back navigation
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  it('should call delete, open snackbar, log out, and navigate to home on delete', () => {
    // Test delete actions
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(mockMatSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
