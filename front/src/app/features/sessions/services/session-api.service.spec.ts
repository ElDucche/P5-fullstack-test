// Remove Angular testing imports if any were present
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';

// Import the service to test
import { SessionApiService } from './session-api.service';
// Import the correct Session interface
import { Session } from '../interfaces/session.interface';

// Mock HttpClient using Jest
const httpClientMock = {
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn()
};

// Cast the mock to the HttpClient type for the service constructor
const mockedHttpClient = httpClientMock as unknown as HttpClient;

describe('SessionApiService (Pure Jest)', () => {
  let service: SessionApiService;
  // Use the correctly typed Jest mock
  let httpClient: jest.Mocked<HttpClient>;
  const apiPath = 'api/session'; // Base path defined in the service

  beforeEach(() => {
    // Reset mocks before each test
    httpClientMock.get.mockReset();
    httpClientMock.post.mockReset();
    httpClientMock.put.mockReset();
    httpClientMock.delete.mockReset();

    // Instantiate the service directly with the mock HttpClient
    service = new SessionApiService(mockedHttpClient);

    // Assign the mock for assertion checks
    httpClient = httpClientMock as unknown as jest.Mocked<HttpClient>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Jest assertion
  });

  // --- all() Method Tests ---
  describe('all', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', description: 'Desc 1', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() },
      { id: 2, name: 'Session 2', description: 'Desc 2', date: new Date(), teacher_id: 2, users: [1], createdAt: new Date(), updatedAt: new Date() }
    ];

    it('should call httpClient.get with the correct URL and return sessions', (done) => {
      httpClient.get.mockReturnValue(of(mockSessions));

      service.all().subscribe({
        next: (sessions) => {
          expect(sessions).toEqual(mockSessions);
          expect(httpClient.get).toHaveBeenCalledWith(apiPath);
          expect(httpClient.get).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during all()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 500, statusText: 'Server Error' });
      httpClient.get.mockReturnValue(throwError(() => errorResponse));

      service.all().subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.get).toHaveBeenCalledWith(apiPath);
          expect(httpClient.get).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- detail() Method Tests ---
  describe('detail', () => {
    const sessionId = '1';
    const detailUrl = `${apiPath}/${sessionId}`;
    const mockSession: Session = { id: 1, name: 'Session 1', description: 'Desc 1', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() };

    it('should call httpClient.get with the correct URL and return a session', (done) => {
      httpClient.get.mockReturnValue(of(mockSession));

      service.detail(sessionId).subscribe({
        next: (session) => {
          expect(session).toEqual(mockSession);
          expect(httpClient.get).toHaveBeenCalledWith(detailUrl);
          expect(httpClient.get).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during detail()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 404, statusText: 'Not Found' });
      httpClient.get.mockReturnValue(throwError(() => errorResponse));

      service.detail(sessionId).subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.get).toHaveBeenCalledWith(detailUrl);
          expect(httpClient.get).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- delete() Method Tests ---
  describe('delete', () => {
    const sessionId = '1';
    const deleteUrl = `${apiPath}/${sessionId}`;
    const mockResponse = { message: 'Deleted' }; // Or whatever the API returns, often it's just status 200 OK with no body

    it('should call httpClient.delete with the correct URL', (done) => {
      // Simulate a 200 OK with no body, which results in null for the observable
      httpClient.delete.mockReturnValue(of(null));

      service.delete(sessionId).subscribe({
        next: (response) => {
          expect(response).toBeNull(); // Or check based on actual API behavior
          expect(httpClient.delete).toHaveBeenCalledWith(deleteUrl);
          expect(httpClient.delete).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during delete()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 500, statusText: 'Server Error' });
      httpClient.delete.mockReturnValue(throwError(() => errorResponse));

      service.delete(sessionId).subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.delete).toHaveBeenCalledWith(deleteUrl);
          expect(httpClient.delete).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- create() Method Tests ---
  describe('create', () => {
    // Note: The Session interface likely doesn't include id, createdAt, updatedAt for creation
    const newSessionData = { name: 'New Session', description: 'New Desc', date: new Date(), teacher_id: 1 };
    const createdSession: Session = { ...newSessionData, id: 3, users: [], createdAt: new Date(), updatedAt: new Date() }; // Mock response with ID and defaults

    it('should call httpClient.post with the correct URL and body, returning the created session', (done) => {
      httpClient.post.mockReturnValue(of(createdSession));

      // Cast the input data to Session for the service call if needed,
      // or adjust the mock data to fully match the Session interface if required by the method signature.
      service.create(newSessionData as Session).subscribe({
        next: (session) => {
          expect(session).toEqual(createdSession);
          expect(httpClient.post).toHaveBeenCalledWith(apiPath, newSessionData);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during create()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 400, statusText: 'Bad Request' });
      httpClient.post.mockReturnValue(throwError(() => errorResponse));

      service.create(newSessionData as Session).subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.post).toHaveBeenCalledWith(apiPath, newSessionData);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- update() Method Tests ---
  describe('update', () => {
    const sessionId = '1';
    const updateUrl = `${apiPath}/${sessionId}`;
    // Data sent for update might not include id, createdAt, updatedAt
    const sessionUpdateData = { name: 'Updated Session', description: 'Updated Desc', date: new Date(), teacher_id: 1 };
    const updatedSessionResponse: Session = { ...sessionUpdateData, id: 1, users: [], createdAt: new Date(), updatedAt: new Date() }; // Mock response

    it('should call httpClient.put with the correct URL and body, returning the updated session', (done) => {
      httpClient.put.mockReturnValue(of(updatedSessionResponse));

      service.update(sessionId, sessionUpdateData as Session).subscribe({
        next: (session) => {
          expect(session).toEqual(updatedSessionResponse);
          expect(httpClient.put).toHaveBeenCalledWith(updateUrl, sessionUpdateData);
          expect(httpClient.put).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during update()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 400, statusText: 'Bad Request' });
      httpClient.put.mockReturnValue(throwError(() => errorResponse));

      service.update(sessionId, sessionUpdateData as Session).subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.put).toHaveBeenCalledWith(updateUrl, sessionUpdateData);
          expect(httpClient.put).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- participate() Method Tests ---
  describe('participate', () => {
    const sessionId = '1';
    const userId = '10';
    const participateUrl = `${apiPath}/${sessionId}/participate/${userId}`;

    it('should call httpClient.post with the correct URL and null body', (done) => {
      httpClient.post.mockReturnValue(of(undefined)); // Expect void response

      service.participate(sessionId, userId).subscribe({
        next: (response) => {
          expect(response).toBeUndefined();
          expect(httpClient.post).toHaveBeenCalledWith(participateUrl, null);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during participate()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 409, statusText: 'Conflict' }); // e.g., already participating
      httpClient.post.mockReturnValue(throwError(() => errorResponse));

      service.participate(sessionId, userId).subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.post).toHaveBeenCalledWith(participateUrl, null);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- unParticipate() Method Tests ---
  describe('unParticipate', () => {
    const sessionId = '1';
    const userId = '10';
    const unParticipateUrl = `${apiPath}/${sessionId}/participate/${userId}`;

    it('should call httpClient.delete with the correct URL', (done) => {
      httpClient.delete.mockReturnValue(of(undefined)); // Expect void response

      service.unParticipate(sessionId, userId).subscribe({
        next: (response) => {
          expect(response).toBeUndefined();
          expect(httpClient.delete).toHaveBeenCalledWith(unParticipateUrl);
          expect(httpClient.delete).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err)
      });
    });

    it('should handle error during unParticipate()', (done) => {
      const errorResponse = new HttpErrorResponse({ status: 404, statusText: 'Not Found' }); // e.g., not participating
      httpClient.delete.mockReturnValue(throwError(() => errorResponse));

      service.unParticipate(sessionId, userId).subscribe({
        next: () => done.fail('should have failed'),
        error: (error) => {
          expect(error).toBe(errorResponse);
          expect(httpClient.delete).toHaveBeenCalledWith(unParticipateUrl);
          expect(httpClient.delete).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });
});