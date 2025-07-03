import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button'; // Import MatButtonModule
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service'; // Import TeacherService
import { SessionApiService } from '../../services/session-api.service'; // Import SessionApiService
import { Session } from '../../interfaces/session.interface'; // Import Session interface
import { Teacher } from '../../../../interfaces/teacher.interface'; // Import Teacher interface
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface'; // Import SessionInformation

import { DetailComponent } from './detail.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// --- Mocks ---
const mockSessionId = '123';
const mockUserId = 1;
const mockTeacherId = 99;

const mockSessionInformation: SessionInformation = {
  admin: true,
  id: mockUserId,
  token: 'token',
  type: 'bearer',
  username: 'adminUser',
  firstName: 'Admin',
  lastName: 'User'
};

const mockSession: Session = {
  id: parseInt(mockSessionId, 10),
  name: 'Test Session',
  description: 'Session Description',
  date: new Date(),
  teacher_id: mockTeacherId,
  users: [] // Start with empty users
};

const mockTeacher: Teacher = {
  id: mockTeacherId,
  lastName: 'Doe',
  firstName: 'John',
  createdAt: new Date(),
  updatedAt: new Date()
};

const mockSessionService = {
  sessionInformation: mockSessionInformation,
  logOut: jest.fn(),
  $isLogged: jest.fn().mockReturnValue(of(true))
};

const mockSessionApiService = {
  detail: jest.fn().mockReturnValue(of({ ...mockSession })), // Return a copy
  delete: jest.fn().mockReturnValue(of({})),
  participate: jest.fn().mockReturnValue(of(undefined)),
  unParticipate: jest.fn().mockReturnValue(of(undefined))
};

const mockTeacherService = {
  detail: jest.fn().mockReturnValue(of({ ...mockTeacher })) // Return a copy
};

const mockRouter = {
  navigate: jest.fn()
};

const mockMatSnackBar = {
  open: jest.fn()
};

// Mock ActivatedRoute
const mockActivatedRoute = {
  snapshot: {
    paramMap: {
      get: jest.fn().mockReturnValue(mockSessionId)
    }
  }
};

// Mock window.history
const mockWindowHistory = {
  back: jest.fn()
};

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let sessionService: SessionService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatButtonModule,
        NoopAnimationsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);

    // Replace the real window.history with our mock
    Object.defineProperty(window, 'history', {
      value: mockWindowHistory
    });

    fixture.detectChanges(); // Trigger ngOnInit
  });

  afterEach(() => {
    jest.clearAllMocks(); // Clean up mocks after each test
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session and teacher details on init', () => {
    expect(sessionApiService.detail).toHaveBeenCalledWith(mockSessionId);
    expect(teacherService.detail).toHaveBeenCalledWith(mockTeacherId.toString());
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

  it('should call window.history.back() when back() is called', () => {
    component.back();
    expect(mockWindowHistory.back).toHaveBeenCalled();
  });

  describe('Admin actions', () => {
    beforeEach(() => {
      component.isAdmin = true;
      fixture.detectChanges();
    });

    it('should call delete service, show snackbar, and navigate on delete()', () => {
      component.delete();
      expect(sessionApiService.delete).toHaveBeenCalledWith(mockSessionId);
      expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('User participation', () => {
    beforeEach(() => {
      component.isAdmin = false;
      // Reset session to not include the user initially
      mockSession.users = [];
      component.isParticipate = false;
      fixture.detectChanges();
    });

    it('should call participate, then fetchSession, and update the UI', fakeAsync(() => {
      const participateButton = fixture.debugElement.nativeElement.querySelector('button[color="primary"]');
      expect(participateButton.textContent).toContain('Participate');

      // Mock the session state AFTER participation for the refresh call
      const sessionAfterParticipate = { ...mockSession, users: [mockUserId] };
      (sessionApiService.detail as jest.Mock).mockReturnValue(of(sessionAfterParticipate));

      component.participate();
      tick(); // Complete the participate call
      fixture.detectChanges(); // Update the view with the new session data

      expect(sessionApiService.participate).toHaveBeenCalledWith(mockSessionId, mockUserId.toString());
      expect(sessionApiService.detail).toHaveBeenCalledTimes(2); // Initial + refresh
      expect(component.isParticipate).toBe(true);

      const unParticipateButton = fixture.debugElement.nativeElement.querySelector('button[color="warn"]');
      expect(unParticipateButton.textContent).toContain('Do not participate');
    }));

    it('should call unParticipate, then fetchSession, and update the UI', fakeAsync(() => {
      // Set initial state to already participating
      component.isParticipate = true;
      fixture.detectChanges();

      const unParticipateButton = fixture.debugElement.nativeElement.querySelector('button[color="warn"]');
      expect(unParticipateButton.textContent).toContain('Do not participate');

      // Mock the session state AFTER un-participating for the refresh call
      const sessionAfterUnParticipate = { ...mockSession, users: [] };
      (sessionApiService.detail as jest.Mock).mockReturnValue(of(sessionAfterUnParticipate));

      component.unParticipate();
      tick(); // Complete the unParticipate call
      fixture.detectChanges(); // Update the view

      expect(sessionApiService.unParticipate).toHaveBeenCalledWith(mockSessionId, mockUserId.toString());
      expect(sessionApiService.detail).toHaveBeenCalledTimes(2); // Initial + refresh
      expect(component.isParticipate).toBe(false);

      const participateButton = fixture.debugElement.nativeElement.querySelector('button[color="primary"]');
      expect(participateButton.textContent).toContain('Participate');
    }));
  });

});