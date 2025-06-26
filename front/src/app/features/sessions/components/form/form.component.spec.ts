import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import {  NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router } from '@angular/router'; 
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs'; 
import { SessionService } from 'src/app/services/session.service';
import { TeacherService } from 'src/app/services/teacher.service'; 
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface'; 
import { Teacher } from 'src/app/interfaces/teacher.interface'; 
import { FormComponent } from './form.component';

const mockSessionId = '42';
const mockUserId = 1;

const mockSessionInformationAdmin = {
  admin: true,
  id: mockUserId,
  token: 'token-admin',
  type: 'bearer',
  username: 'admin',
  firstName: 'Admin',
  lastName: 'User'
};

const mockSessionInformationUser = {
  admin: false,
  id: mockUserId + 1,
  token: 'token-user',
  type: 'bearer',
  username: 'user',
  firstName: 'Regular',
  lastName: 'User'
};

const mockTeachers: Teacher[] = [
  { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date() },
  { id: 2, lastName: 'Smith', firstName: 'Jane', createdAt: new Date(), updatedAt: new Date() },
];

const mockSession: Session = {
  id: parseInt(mockSessionId, 10),
  name: 'Existing Session',
  description: 'Existing Description',
  date: new Date(2025, 5, 15), // Use a specific date
  teacher_id: mockTeachers[0].id!,
  users: [mockUserId]
};

const mockSessionApiService = {
  detail: jest.fn().mockReturnValue(of({ ...mockSession })),
  create: jest.fn().mockReturnValue(of({ ...mockSession, id: 999 })), // Return a mock created session
  update: jest.fn().mockReturnValue(of({ ...mockSession })) // Return the updated session
};

const mockTeacherService = {
  all: jest.fn().mockReturnValue(of([...mockTeachers]))
};

const mockRouter = {
  navigate: jest.fn(),
  url: '/sessions/create' // Default to create mode
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

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let sessionService: SessionService;
  let router: Router;
  let snackBar: MatSnackBar;
  let route: ActivatedRoute;
  let fb: FormBuilder;

  // Helper function to configure TestBed
  const configureTestBed = async (isAdmin: boolean, url: string) => {
    // Reset mocks for each configuration
    mockSessionApiService.detail.mockClear().mockReturnValue(of({ ...mockSession }));
    mockSessionApiService.create.mockClear().mockReturnValue(of({ ...mockSession, id: 999 }));
    mockSessionApiService.update.mockClear().mockReturnValue(of({ ...mockSession }));
    mockTeacherService.all.mockClear().mockReturnValue(of([...mockTeachers]));
    mockRouter.navigate.mockClear();
    mockRouter.url = url; // Set URL for the test context
    mockMatSnackBar.open.mockClear();
    mockActivatedRoute.snapshot.paramMap.get.mockClear().mockReturnValue(mockSessionId);

    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        NoopAnimationsModule, // Use NoopAnimationsModule
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSnackBarModule,
        MatSelectModule
      ],
      providers: [
        {
          provide: SessionService,
          useValue: { sessionInformation: isAdmin ? { ...mockSessionInformationAdmin } : { ...mockSessionInformationUser } }
        },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        FormBuilder // Provide FormBuilder
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    // Inject services
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(MatSnackBar);
    route = TestBed.inject(ActivatedRoute);
    fb = TestBed.inject(FormBuilder);
  };

  describe('Common Initialization', () => {
    beforeEach(async () => {
      await configureTestBed(true, '/sessions/create'); // Default admin, create mode
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should assign teachers$ observable from teacherService.all()', () => {
      fixture.detectChanges(); // Trigger ngOnInit
      expect(teacherService.all).toHaveBeenCalled();
      // Check if the observable is assigned (can check instance type or subscribe)
      component.teachers$.subscribe(teachers => {
        expect(teachers).toEqual(mockTeachers);
      });
    });
  });

  describe('Admin Guard', () => {
    it('should navigate to /sessions if user is not admin', async () => {
      await configureTestBed(false, '/sessions/create'); // Non-admin user
      fixture.detectChanges(); // Trigger ngOnInit
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should NOT navigate if user is admin', async () => {
      await configureTestBed(true, '/sessions/create'); // Admin user
      fixture.detectChanges(); // Trigger ngOnInit
      expect(router.navigate).not.toHaveBeenCalledWith(['/sessions']);
    });
  });

  describe('Create Mode', () => {
    beforeEach(async () => {
      await configureTestBed(true, '/sessions/create'); // Admin, create URL
      fixture.detectChanges(); // Trigger ngOnInit
    });

    it('should set onUpdate to false', () => {
      expect(component.onUpdate).toBe(false);
    });

    it('should initialize form with empty values', () => {
      expect(component.sessionForm).toBeDefined();
      expect(component.sessionForm?.value).toEqual({
        name: '',
        date: '',
        teacher_id: '',
        description: ''
      });
    });

    it('should NOT call sessionApiService.detail', () => {
      expect(sessionApiService.detail).not.toHaveBeenCalled();
    });

    it('should call sessionApiService.create and exitPage on submit', fakeAsync(() => {
      const exitPageSpy = jest.spyOn(component as any, 'exitPage');
      // Set valid form data
      component.sessionForm?.controls['name'].setValue('New Session');
      component.sessionForm?.controls['date'].setValue('2025-10-20');
      component.sessionForm?.controls['teacher_id'].setValue(mockTeachers[1].id);
      component.sessionForm?.controls['description'].setValue('New Description');

      component.submit();
      tick(); // Resolve create observable

      expect(sessionApiService.create).toHaveBeenCalledWith(component.sessionForm?.value);
      expect(exitPageSpy).toHaveBeenCalledWith('Session created !');
    }));
  });

  describe('Update Mode', () => {
    beforeEach(async () => {
      await configureTestBed(true, '/sessions/update/' + mockSessionId); // Admin, update URL
      fixture.detectChanges(); // Trigger ngOnInit
    });

    it('should set onUpdate to true', () => {
      expect(component.onUpdate).toBe(true);
    });

    it('should get session id from route', () => {
      expect(route.snapshot.paramMap.get).toHaveBeenCalledWith('id');
      expect((component as any).id).toBe(mockSessionId);
    });

    it('should call sessionApiService.detail with id', () => {
      expect(sessionApiService.detail).toHaveBeenCalledWith(mockSessionId);
    });

    it('should initialize form with session data after detail call', fakeAsync(() => {
      tick(); // Resolve detail observable
      fixture.detectChanges(); // Update view with form data

      expect(component.sessionForm).toBeDefined();
      expect(component.sessionForm?.value).toEqual({
        name: mockSession.name,
        date: '2025-06-14', // Check formatted date
        teacher_id: mockSession.teacher_id,
        description: mockSession.description
      });
    }));

    it('should call sessionApiService.update and exitPage on submit', fakeAsync(() => {
      tick(); // Resolve detail observable to init form
      const exitPageSpy = jest.spyOn(component as any, 'exitPage');
      // Modify form data slightly
      component.sessionForm?.controls['name'].setValue('Updated Session Name');

      component.submit();
      tick(); // Resolve update observable

      expect(sessionApiService.update).toHaveBeenCalledWith(mockSessionId, component.sessionForm?.value);
      expect(exitPageSpy).toHaveBeenCalledWith('Session updated !');
    }));

  });

  describe('exitPage', () => {
    beforeEach(async () => {
      await configureTestBed(true, '/sessions/create');
      fixture.detectChanges();
    });

    it('should call snackBar.open and router.navigate', () => {
      const message = 'Test Message';
      (component as any).exitPage(message); // Call private method for test

      expect(snackBar.open).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  // Add form validation tests if needed
  describe('Form Validation', () => {
     beforeEach(async () => {
      await configureTestBed(true, '/sessions/create');
      fixture.detectChanges();
    });

    it('should require name', () => {
        const nameControl = component.sessionForm?.controls['name'];
        nameControl?.setValue('');
        expect(nameControl?.valid).toBeFalsy();
        expect(nameControl?.hasError('required')).toBeTruthy();
    });

     it('should require date', () => {
        const dateControl = component.sessionForm?.controls['date'];
        dateControl?.setValue('');
        expect(dateControl?.valid).toBeFalsy();
        expect(dateControl?.hasError('required')).toBeTruthy();
    });

     it('should require teacher_id', () => {
        const teacherControl = component.sessionForm?.controls['teacher_id'];
        teacherControl?.setValue(''); // Or null depending on select behavior
        expect(teacherControl?.valid).toBeFalsy();
        expect(teacherControl?.hasError('required')).toBeTruthy();
    });

     it('should require description', () => {
        const descriptionControl = component.sessionForm?.controls['description'];
        descriptionControl?.setValue('');
        expect(descriptionControl?.valid).toBeFalsy();
        expect(descriptionControl?.hasError('required')).toBeTruthy();
    });

    // Add max length test for description if desired
  });
});