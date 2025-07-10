import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms'; // Import FormBuilder
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations'; // Import NoopAnimationsModule
import { Router } from '@angular/router'; // Import Router
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs'; // Import of and throwError
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service'; // Import AuthService
import { LoginRequest } from '../../interfaces/loginRequest.interface'; // Import LoginRequest

import { LoginComponent } from './login.component';

// --- Mocks ---
const mockAuthService = {
  login: jest.fn()
};

const mockSessionService = {
  logIn: jest.fn(),
  sessionInformation: undefined, // Add properties if needed elsewhere
  isLogged: false,
  $isLogged: jest.fn().mockReturnValue(of(false))
};

const mockRouter = {
  navigate: jest.fn()
};

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    // Reset mocks before each test
    mockAuthService.login.mockClear();
    mockSessionService.logIn.mockClear();
    mockRouter.navigate.mockClear();

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule, // Use ReactiveFormsModule for form controls
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule // Disable animations for testing
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
        // FormBuilder is provided by ReactiveFormsModule
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    
    fixture.detectChanges(); // Trigger initial data binding and ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Form validation', () => {
    it('should have an invalid form when empty', () => {
      expect(component.form.valid).toBeFalsy();
    });

    it('should have an invalid email field if email is not a valid format', () => {
      const email = component.form.get('email');
      email?.setValue('not-an-email');
      expect(email?.valid).toBeFalsy();
    });

    it('should have a required password field', () => {
      const password = component.form.get('password');
      password?.setValue('');
      expect(password?.valid).toBeFalsy();
    });

    it('should have a valid form when email and password are correct', () => {
      component.form.get('email')?.setValue('test@example.com');
      component.form.get('password')?.setValue('password123');
      expect(component.form.valid).toBeTruthy();
    });
  });

  describe('Form submission', () => {
    beforeEach(() => {
      // Fill the form to make it valid before submission tests
      component.form.get('email')?.setValue('test@example.com');
      component.form.get('password')?.setValue('password123');
    });

    it('should call authService.login on submit', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password123'
      };
      mockAuthService.login.mockReturnValue(of({} as SessionInformation)); // Mock successful login

      component.submit();

      expect(authService.login).toHaveBeenCalledWith(loginRequest);
    });

    it('should call sessionService.logIn and router.navigate on successful login', () => {
      const sessionInfo: SessionInformation = { token: 'xyz', type: 'bearer', id: 1, username: 'test', firstName: 'Test', lastName: 'User', admin: false };
      mockAuthService.login.mockReturnValue(of(sessionInfo));

      component.submit();

      expect(sessionService.logIn).toHaveBeenCalledWith(sessionInfo);
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBeFalsy();
    });

    it('should set onError to true on failed login', () => {
      mockAuthService.login.mockReturnValue(throwError(() => new Error('Login failed')));

      component.submit();

      expect(component.onError).toBeTruthy();
      expect(sessionService.logIn).not.toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('DOM Interaction', () => {
    it('should disable the submit button when the form is invalid', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      const submitButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
      
      // Form is initially invalid
      expect(submitButton.disabled).toBeTruthy();

      // Fill the form to make it valid
      component.form.get('email')?.setValue('test@example.com');
      component.form.get('password')?.setValue('password123');
      fixture.detectChanges(); // Update the DOM

      expect(submitButton.disabled).toBeFalsy();
    });

    it('should display an error message if onError is true', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      
      // Initially, no error message
      expect(compiled.querySelector('.error')).toBeNull();

      // Set error state and update DOM
      component.onError = true;
      fixture.detectChanges();

      const errorMessage = compiled.querySelector('.error');
      expect(errorMessage).not.toBeNull();
      expect(errorMessage?.textContent).toContain('An error occurred');
    });
  });
});