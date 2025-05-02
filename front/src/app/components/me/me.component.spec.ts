import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing'; // Import RouterTestingModule
import { expect } from '@jest/globals'; // Jest expect
import { of, throwError } from 'rxjs'; // Import of and throwError for mocking observables
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service'; // Import UserService
import { User } from 'src/app/interfaces/user.interface'; // Import User interface

import { MeComponent } from './me.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations'; // Import NoopAnimationsModule

// --- Mocks ---
const mockSessionInformation = {
  admin: true,
  id: 1,
  token: 'token',
  type: 'bearer',
  username: 'test',
  firstName: 'Test',
  lastName: 'User'
};

const mockSessionService = {
  sessionInformation: mockSessionInformation,
  logOut: jest.fn(),
  $isLogged: jest.fn().mockReturnValue(of(true)) // Add if needed by template/other logic
};

const mockUserService = {
  getById: jest.fn(),
  delete: jest.fn()
};

const mockRouter = {
  navigate: jest.fn()
};

const mockMatSnackBar = {
  open: jest.fn()
};

// Mock window.history
const mockWindowHistory = {
  back: jest.fn()
};

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let router: Router;
  let snackBar: MatSnackBar;

  // Backup original window.history and restore after tests
  const originalWindowHistory = window.history;
  beforeAll(() => {
    Object.defineProperty(window, 'history', {
      value: mockWindowHistory
    });
  });

  afterAll(() => {
    Object.defineProperty(window, 'history', {
      value: originalWindowHistory
    });
  });


  beforeEach(async () => {
    // Reset mocks
    mockSessionService.logOut.mockClear();
    mockUserService.getById.mockClear();
    mockUserService.delete.mockClear();
    mockRouter.navigate.mockClear();
    mockMatSnackBar.open.mockClear();
    mockWindowHistory.back.mockClear(); // Reset history mock

    // Default mock implementations
    const mockUser: User = {
      id: 1,
      email: 'test@example.com',
      lastName: 'User',
      firstName: 'Test',
      admin: true,
      password: '', // Usually not needed/returned
      createdAt: new Date(),
      updatedAt: new Date()
    };
    mockUserService.getById.mockReturnValue(of(mockUser)); // Default success for getById
    mockUserService.delete.mockReturnValue(of({})); // Default success for delete

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        HttpClientModule, // Often needed by services even if mocked
        RouterTestingModule, // Provides stubs for router directives
        MatSnackBarModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule // Disable animations for testing Material components
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    // Inject services to check calls
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(MatSnackBar);

    // fixture.detectChanges(); // ngOnInit is called here
  });

  it('should create', () => {
    expect(component).toBeTruthy(); // Jest assertion
  });

  describe('ngOnInit', () => {
    it('should call userService.getById with the correct user ID from sessionService', () => {
      fixture.detectChanges(); // Trigger ngOnInit
      expect(userService.getById).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    });

    it('should set the user property with the data returned from userService.getById', () => {
       const expectedUser: User = {
          id: 1, email: 'test@example.com', lastName: 'User', firstName: 'Test', admin: true, password: '',
          createdAt: new Date(), updatedAt: new Date()
       };
       mockUserService.getById.mockReturnValue(of(expectedUser)); // Ensure specific user is returned

       fixture.detectChanges(); // Trigger ngOnInit

       expect(component.user).toEqual(expectedUser);
    });
  });

  describe('back', () => {
    it('should call window.history.back', () => {
      component.back();
      expect(mockWindowHistory.back).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    beforeEach(() => {
        // Ensure user is set before delete tests, as ngOnInit might not have run or failed
        component.user = { id: 1, email: 'test@example.com', lastName: 'User', firstName: 'Test', admin: true, password: '', createdAt: new Date() };
        fixture.detectChanges(); // Needed if template relies on user data
    });

    it('should call userService.delete with the correct user ID', () => {
      component.delete();
      expect(userService.delete).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    });

    it('should call snackBar.open, sessionService.logOut, and router.navigate on successful deletion', () => {
      mockUserService.delete.mockReturnValue(of({})); // Ensure success response

      component.delete();

      expect(snackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
      expect(sessionService.logOut).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/']);
    });

  });
});