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
  let fb: FormBuilder; // To potentially interact with the form directly if needed

  beforeEach(async () => {
    // Reset mocks
    mockAuthService.login.mockClear();
    mockSessionService.logIn.mockClear();
    mockRouter.navigate.mockClear();

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        // Provide mocks for services
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
        // FormBuilder is provided by ReactiveFormsModule
      ],
      imports: [
        RouterTestingModule,
        // BrowserAnimationsModule, // Use NoopAnimationsModule for testing
        NoopAnimationsModule,
        HttpClientModule, // Often needed by services
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule // Essential for form group
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    // Inject services/mocks
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fb = TestBed.inject(FormBuilder); // Inject FormBuilder if needed

    fixture.detectChanges(); // Trigger initial data binding and ngOnInit (if any)
  });

  it('should create', () => {
    expect(component).toBeTruthy(); // Jest assertion
  });

  it('should initialize the form with empty email and password', () => {
    expect(component.form.value).toEqual({ email: '', password: '' });
  });

  it('should initialize with hide=true and onError=false', () => {
    expect(component.hide).toBe(true);
    expect(component.onError).toBe(false);
  });

  describe('submit', () => {
    const testEmail = 'test@example.com';
    const testPassword = 'password123';
    const loginRequest: LoginRequest = { email: testEmail, password: testPassword };

    beforeEach(() => {
      // Set form values before each submit test
      component.form.controls['email'].setValue(testEmail);
      component.form.controls['password'].setValue(testPassword);
    });

    it('should call authService.login with form values', () => {
      // Arrange: Mock login to return an observable to prevent errors during the call
      mockAuthService.login.mockReturnValue(of({} as SessionInformation)); // Return empty object for this check
      // Act
      component.submit();
      // Assert
      expect(authService.login).toHaveBeenCalledWith(loginRequest);
    });

    it('should call sessionService.logIn and router.navigate on successful login', () => {
      // Arrange
      const mockSessionInfo: SessionInformation = {
        token: 'mockToken', type: 'bearer', id: 1, username: 'test', firstName: 'Test', lastName: 'User', admin: false
      };
      mockAuthService.login.mockReturnValue(of(mockSessionInfo)); // Simulate successful login

      // Act
      component.submit();

      // Assert
      expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBe(false); // Ensure onError is not set
    });

    it('should set onError to true on failed login', () => {
      // Arrange
      const errorResponse = new Error('Login failed');
      mockAuthService.login.mockReturnValue(throwError(() => errorResponse)); // Simulate failed login

      // Act
      component.submit();

      // Assert
      expect(component.onError).toBe(true);
      expect(sessionService.logIn).not.toHaveBeenCalled(); // Ensure logIn was not called
      expect(router.navigate).not.toHaveBeenCalled(); // Ensure navigate was not called
    });
  });

  // Optional: Add tests for form validation
  describe('Form Validation', () => {
    it('should require email', () => {
        const emailControl = component.form.controls['email'];
        emailControl.setValue('');
        expect(emailControl.valid).toBeFalsy();
        expect(emailControl.hasError('required')).toBeTruthy();
    });

    it('should require a valid email format', () => {
        const emailControl = component.form.controls['email'];
        emailControl.setValue('not-an-email');
        expect(emailControl.valid).toBeFalsy();
        expect(emailControl.hasError('email')).toBeTruthy();
    });

     it('should require password', () => {
        const passwordControl = component.form.controls['password'];
        passwordControl.setValue('');
        expect(passwordControl.valid).toBeFalsy();
        expect(passwordControl.hasError('required')).toBeTruthy();
    });

    // Add more validation tests if needed (e.g., min length for password)
  });
});