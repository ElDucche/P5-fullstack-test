import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

// Mock HttpClient using Jest
const httpClientMock = {
  post: jest.fn()
};

// Cast the mock to the HttpClient type for the service constructor
const mockedHttpClient = httpClientMock as unknown as HttpClient;

describe('AuthService (Pure Jest)', () => {
  let service: AuthService;
  // Use the correctly typed Jest mock
  let httpClient: jest.Mocked<HttpClient>;
  const apiPath = 'api/auth'; // Base path defined in the service

  beforeEach(() => {
    // Reset mocks before each test
    httpClientMock.post.mockReset();

    // Instantiate the service directly with the mock HttpClient
    service = new AuthService(mockedHttpClient);

    // Assign the mock for assertion checks
    httpClient = httpClientMock as unknown as jest.Mocked<HttpClient>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Jest assertion
  });

  // --- Register Method Tests ---
  describe('register', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'Test',
      lastName: 'User',
      password: 'password123'
    };
    const registerUrl = `${apiPath}/register`;

    it('should call httpClient.post with the correct URL and body for register', (done) => {
      // Arrange: Mock successful response (void means no body, use null or undefined)
      httpClient.post.mockReturnValue(of(undefined));

      // Act
      service.register(registerRequest).subscribe({
        next: (response) => {
          // Assert
          expect(response).toBeUndefined(); // Expect void/undefined response
          expect(httpClient.post).toHaveBeenCalledWith(registerUrl, registerRequest);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err) // Fail test if error occurs
      });
    });

    it('should handle error during registration', (done) => {
      // Arrange: Mock error response
      const errorResponse = new HttpErrorResponse({
        error: 'Registration failed',
        status: 400, statusText: 'Bad Request'
      });
      httpClient.post.mockReturnValue(throwError(() => errorResponse));

      // Act
      service.register(registerRequest).subscribe({
        next: () => done.fail('should have failed with an error'),
        error: (error) => {
          // Assert
          expect(error).toBe(errorResponse);
          expect(httpClient.post).toHaveBeenCalledWith(registerUrl, registerRequest);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });

  // --- Login Method Tests ---
  describe('login', () => {
    const loginRequest: LoginRequest = {
      email: 'test@example.com',
      password: 'password123'
    };
    const loginUrl = `${apiPath}/login`;
    const mockSessionInfo: SessionInformation = {
      token: 'mockToken',
      type: 'bearer',
      id: 1,
      username: 'test@example.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    it('should call httpClient.post with the correct URL and body for login and return SessionInformation', (done) => {
      // Arrange: Mock successful response
      httpClient.post.mockReturnValue(of(mockSessionInfo));

      // Act
      service.login(loginRequest).subscribe({
        next: (response) => {
          // Assert
          expect(response).toEqual(mockSessionInfo); // Expect the session info
          expect(httpClient.post).toHaveBeenCalledWith(loginUrl, loginRequest);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        },
        error: (err) => done.fail(err) // Fail test if error occurs
      });
    });

    it('should handle error during login', (done) => {
      // Arrange: Mock error response
      const errorResponse = new HttpErrorResponse({
        error: 'Login failed',
        status: 401, statusText: 'Unauthorized'
      });
      httpClient.post.mockReturnValue(throwError(() => errorResponse));

      // Act
      service.login(loginRequest).subscribe({
        next: () => done.fail('should have failed with an error'),
        error: (error) => {
          // Assert
          expect(error).toBe(errorResponse);
          expect(httpClient.post).toHaveBeenCalledWith(loginUrl, loginRequest);
          expect(httpClient.post).toHaveBeenCalledTimes(1);
          done();
        }
      });
    });
  });
});