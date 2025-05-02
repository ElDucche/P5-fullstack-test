import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

// Mock HttpClient for Jest
const httpClientMock = {
  get: jest.fn()
};

describe('TeacherService (Jest)', () => {
  let service: TeacherService;
  let httpClient: jest.Mocked<HttpClient>;

  beforeEach(() => {
    httpClientMock.get.mockClear(); // Clear previous calls to the mock
    service = new TeacherService(httpClientMock as unknown as HttpClient);
    httpClient = httpClientMock as unknown as jest.Mocked<HttpClient>;
    });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all teachers', (done) => {
    const mockTeachers: Teacher[] = [
      { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, lastName: 'Smith', firstName: 'Jane', createdAt: new Date(), updatedAt: new Date() },
    ];
    // Configure the mock response
    httpClient.get.mockReturnValue(of(mockTeachers));

    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
      // Verify the mock was called correctly
      expect(httpClient.get).toHaveBeenCalledWith('api/teacher');
      expect(httpClient.get).toHaveBeenCalledTimes(1);
      done(); // Signal async test completion
    });
  });

  it('should fetch teacher details by ID', (done) => {
    const teacherId = '1';
    // Assuming Teacher interface has id as number, adjust mock accordingly
    const mockTeacher: Teacher = { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date() };

    // Configure the mock response
    httpClient.get.mockReturnValue(of(mockTeacher));

    service.detail(teacherId).subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
      // Verify the mock was called correctly
      expect(httpClient.get).toHaveBeenCalledWith(`api/teacher/${teacherId}`);
      expect(httpClient.get).toHaveBeenCalledTimes(1);
      done(); // Signal async test completion
    });
  });

  // Example of an additional test: Error handling for 'all'
  it('should handle error when fetching all teachers', (done) => {
    const errorResponse = new Error('Failed to fetch');
    // Configure the mock to return an error
    httpClient.get.mockReturnValue(throwError(() => errorResponse));

    service.all().subscribe({
        next: () => fail('should have failed with an error'), // Fail test if next is called
        error: (error) => {
            expect(error).toBe(errorResponse);
            expect(httpClient.get).toHaveBeenCalledWith('api/teacher');
            expect(httpClient.get).toHaveBeenCalledTimes(1);
            done(); // Signal async test completion
        }
    });
  });

    // Example of an additional test: Error handling for 'detail'
  it('should handle error when fetching teacher details by ID', (done) => {
    const teacherId = '1';
    const errorResponse = new Error('Teacher not found');
     // Configure the mock to return an error
    httpClient.get.mockReturnValue(throwError(() => errorResponse));

    service.detail(teacherId).subscribe({
        next: () => fail('should have failed with an error'), // Fail test if next is called
        error: (error) => {
            expect(error).toBe(errorResponse);
            expect(httpClient.get).toHaveBeenCalledWith(`api/teacher/${teacherId}`);
            expect(httpClient.get).toHaveBeenCalledTimes(1);
            done(); // Signal async test completion
        }
    });
  });
});