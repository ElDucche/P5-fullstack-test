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
  let snackBar: MatSnackBar;
  let route: ActivatedRoute;

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
    mockSessionApiService.detail.mockClear().mockReturnValue(of({ ...mockSession, users: [] })); // Reset with copy
    mockSessionApiService.delete.mockClear().mockReturnValue(of({}));
    mockSessionApiService.participate.mockClear().mockReturnValue(of(undefined));
    mockSessionApiService.unParticipate.mockClear().mockReturnValue(of(undefined));
    mockTeacherService.detail.mockClear().mockReturnValue(of({ ...mockTeacher })); // Reset with copy
    mockRouter.navigate.mockClear();
    mockMatSnackBar.open.mockClear();
    mockWindowHistory.back.mockClear();
    mockActivatedRoute.snapshot.paramMap.get.mockClear().mockReturnValue(mockSessionId);
    (mockSessionService as any).sessionInformation = { ...mockSessionInformation }; // Reset session info

    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule, // Although no form, it might be needed by other imports indirectly or future use
        NoopAnimationsModule, // Disable animations
        // Import necessary Material modules used in the template
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        MatFormFieldModule, // If used
        MatInputModule      // If used
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Inject services
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(MatSnackBar);
    route = TestBed.inject(ActivatedRoute);

    // Initial detectChanges calls ngOnInit
    // fixture.detectChanges(); // Moved inside tests where ngOnInit logic needs checking
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize properties from route and sessionService in constructor', () => {
    expect(route.snapshot.paramMap.get).toHaveBeenCalledWith('id');
    expect(component.sessionId).toBe(mockSessionId);
    expect(component.isAdmin).toBe(mockSessionInformation.admin);
    expect(component.userId).toBe(mockSessionInformation.id.toString());
  });

  describe('ngOnInit & fetchSession', () => {
    it('should call fetchSession on init', () => {
      const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
      fixture.detectChanges(); // Trigger ngOnInit
      expect(fetchSessionSpy).toHaveBeenCalled();
    });

    it('should call sessionApiService.detail with sessionId', fakeAsync(() => {
      fixture.detectChanges(); // Trigger ngOnInit -> fetchSession
      tick(); // Allow observables to resolve
      expect(sessionApiService.detail).toHaveBeenCalledWith(mockSessionId);
    }));

    it('should set session and call teacherService.detail on successful fetch', fakeAsync(() => {
      const sessionData = { ...mockSession, users: [] };
      mockSessionApiService.detail.mockReturnValue(of(sessionData));
      fixture.detectChanges();
      tick();

      expect(component.session).toEqual(sessionData);
      expect(teacherService.detail).toHaveBeenCalledWith(sessionData.teacher_id.toString());
    }));

    it('should set teacher on successful teacherService.detail', fakeAsync(() => {
      const teacherData = { ...mockTeacher };
      mockTeacherService.detail.mockReturnValue(of(teacherData));
      fixture.detectChanges();
      tick(); // Resolve sessionApiService.detail
      tick(); // Resolve teacherService.detail

      expect(component.teacher).toEqual(teacherData);
    }));

    it('should set isParticipate to true if user ID is in session users', fakeAsync(() => {
      const sessionData = { ...mockSession, users: [mockUserId, 999] };
      mockSessionApiService.detail.mockReturnValue(of(sessionData));
      fixture.detectChanges();
      tick();

      expect(component.isParticipate).toBe(true);
    }));

    it('should set isParticipate to false if user ID is not in session users', fakeAsync(() => {
      const sessionData = { ...mockSession, users: [998, 999] };
      mockSessionApiService.detail.mockReturnValue(of(sessionData));
      fixture.detectChanges();
      tick();

      expect(component.isParticipate).toBe(false);
    }));

  });

  describe('back', () => {
    it('should call window.history.back', () => {
      component.back();
      expect(mockWindowHistory.back).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    it('should call sessionApiService.delete with sessionId', () => {
      component.delete();
      expect(sessionApiService.delete).toHaveBeenCalledWith(mockSessionId);
    });

    it('should show snackbar and navigate on successful delete', fakeAsync(() => {
      component.delete();
      tick(); // Resolve delete observable

      expect(snackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    }));

  });

  describe('participate', () => {
    it('should call sessionApiService.participate with sessionId and userId', () => {
      component.participate();
      expect(sessionApiService.participate).toHaveBeenCalledWith(mockSessionId, mockUserId.toString());
    });

  });

  describe('unParticipate', () => {
    it('should call sessionApiService.unParticipate with sessionId and userId', () => {
      component.unParticipate();
      expect(sessionApiService.unParticipate).toHaveBeenCalledWith(mockSessionId, mockUserId.toString());
    });

  });
});