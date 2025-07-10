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

  beforeEach(async () => {
    // Reset mocks
    mockAuthService.register.mockClear();
    mockRouter.navigate.mockClear();

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Form validation', () => {
    it('should have an invalid form when empty', () => {
      expect(component.form.valid).toBeFalsy();
    });

    it('should require email, firstName, lastName, and password', () => {
      const email = component.form.get('email');
      const firstName = component.form.get('firstName');
      const lastName = component.form.get('lastName');
      const password = component.form.get('password');

      // Set one field and check if the form is still invalid
      email?.setValue('test@test.com');
      expect(component.form.valid).toBeFalsy();

      firstName?.setValue('Test');
      expect(component.form.valid).toBeFalsy();

      lastName?.setValue('User');
      expect(component.form.valid).toBeFalsy();

      // All fields except password are set
      password?.setValue('');
      expect(password?.valid).toBeFalsy();
      expect(component.form.valid).toBeFalsy();
    });

    it('should have a valid form when all fields are filled correctly', () => {
      component.form.get('email')?.setValue('test@example.com');
      component.form.get('firstName')?.setValue('Test');
      component.form.get('lastName')?.setValue('User');
      component.form.get('password')?.setValue('password123');
      expect(component.form.valid).toBeTruthy();
    });
  });

  describe('Form submission', () => {
    beforeEach(() => {
      // Fill the form to make it valid
      component.form.get('email')?.setValue('test@example.com');
      component.form.get('firstName')?.setValue('Test');
      component.form.get('lastName')?.setValue('User');
      component.form.get('password')?.setValue('password123');
    });

    it('should call authService.register on submit', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@example.com',
        firstName: 'Test',
        lastName: 'User',
        password: 'password123'
      };
      mockAuthService.register.mockReturnValue(of(undefined)); // Mock successful registration

      component.submit();

      expect(authService.register).toHaveBeenCalledWith(registerRequest);
    });

    it('should navigate to /login on successful registration', () => {
      mockAuthService.register.mockReturnValue(of(undefined));

      component.submit();

      expect(router.navigate).toHaveBeenCalledWith(['/login']);
      expect(component.onError).toBeFalsy();
    });

    it('should set onError to true on failed registration', () => {
      mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));

      component.submit();

      expect(component.onError).toBeTruthy();
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('DOM Interaction', () => {
    it('should disable the submit button when the form is invalid', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      const submitButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
      
      expect(submitButton.disabled).toBeTruthy();

      component.form.get('email')?.setValue('test@example.com');
      component.form.get('firstName')?.setValue('Test');
      component.form.get('lastName')?.setValue('User');
      component.form.get('password')?.setValue('password123');
      fixture.detectChanges();

      expect(submitButton.disabled).toBeFalsy();
    });

    it('should display an error message if onError is true', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('.error')).toBeNull();

      component.onError = true;
      fixture.detectChanges();

      const errorMessage = compiled.querySelector('.error');
      expect(errorMessage).not.toBeNull();
      expect(errorMessage?.textContent).toContain('An error occurred');
    });
  });
});