import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
// Use NoopAnimationsModule instead of BrowserAnimationsModule for testing
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router'; // Import Router
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs'; // Import of and throwError
import { AuthService } from '../../services/auth.service'; // Import AuthService
import { RegisterRequest } from '../../interfaces/registerRequest.interface'; // Import RegisterRequest

import { RegisterComponent } from './register.component';

// --- Mocks ---
const mockAuthService = {
  register: jest.fn()
};

const mockRouter = {
  navigate: jest.fn()
};

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let fb: FormBuilder;

  beforeEach(async () => {
    // Reset mocks
    mockAuthService.register.mockClear();
    mockRouter.navigate.mockClear();

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        // Use NoopAnimationsModule for testing
        NoopAnimationsModule,
        RouterTestingModule,
        HttpClientModule, // Often needed by services
        ReactiveFormsModule, // Essential for form group
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        // Provide mocks for services
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        // FormBuilder is provided by ReactiveFormsModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    // Inject services/mocks
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fb = TestBed.inject(FormBuilder);

    fixture.detectChanges(); // Trigger initial data binding and ngOnInit (if any)
  });

  it('should create', () => {
    expect(component).toBeTruthy(); // Jest assertion
  });

  it('should initialize the form with empty fields', () => {
    expect(component.form.value).toEqual({ email: '', firstName: '', lastName: '', password: '' });
  });

  it('should initialize with onError=false', () => {
    expect(component.onError).toBe(false);
  });

  describe('submit', () => {
    const testEmail = 'test@example.com';
    const testFirstName = 'Test';
    const testLastName = 'User';
    const testPassword = 'password123';
    const registerRequest: RegisterRequest = {
      email: testEmail,
      firstName: testFirstName,
      lastName: testLastName,
      password: testPassword
    };

    beforeEach(() => {
      // Set form values before each submit test
      component.form.controls['email'].setValue(testEmail);
      component.form.controls['firstName'].setValue(testFirstName);
      component.form.controls['lastName'].setValue(testLastName);
      component.form.controls['password'].setValue(testPassword);
    });

    it('should call authService.register with form values', () => {
      // Arrange: Mock register to return an observable
      mockAuthService.register.mockReturnValue(of(undefined)); // Simulate success (API returns void)

      // Act
      component.submit();

      // Assert
      expect(authService.register).toHaveBeenCalledWith(registerRequest);
    });

    it('should navigate to /login on successful registration', () => {
      // Arrange
      mockAuthService.register.mockReturnValue(of(undefined)); // Simulate success

      // Act
      component.submit();

      // Assert
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
      expect(component.onError).toBe(false); // Ensure onError is not set
    });

    it('should set onError to true on failed registration', () => {
      // Arrange
      const errorResponse = new Error('Registration failed');
      mockAuthService.register.mockReturnValue(throwError(() => errorResponse)); // Simulate failure

      // Act
      component.submit();

      // Assert
      expect(component.onError).toBe(true);
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

     it('should require firstName', () => {
        const firstNameControl = component.form.controls['firstName'];
        firstNameControl.setValue('');
        expect(firstNameControl.valid).toBeFalsy();
        expect(firstNameControl.hasError('required')).toBeTruthy();
    });

     it('should require lastName', () => {
        const lastNameControl = component.form.controls['lastName'];
        lastNameControl.setValue('');
        expect(lastNameControl.valid).toBeFalsy();
        expect(lastNameControl.hasError('required')).toBeTruthy();
    });

     it('should require password', () => {
        const passwordControl = component.form.controls['password'];
        passwordControl.setValue('');
        expect(passwordControl.valid).toBeFalsy();
        expect(passwordControl.hasError('required')).toBeTruthy();
    });

    // Add more validation tests if needed (min/max length)
  });
});