import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

// Mock HttpClient using Jest
const httpClientMock = {
  get: jest.fn(),
  delete: jest.fn() // Add mock for delete method
};

// Cast the mock to the HttpClient type for the service constructor
const mockedHttpClient = httpClientMock as unknown as HttpClient;

describe('UserService (Pure Jest)', () => {
  let service: UserService;
  // Use the correctly typed Jest mock
  let httpClient: jest.Mocked<HttpClient>;
  const apiPath = 'api/user'; // Define base path

  beforeEach(() => {
    // Reset mocks before each test
    httpClientMock.get.mockReset();
    httpClientMock.delete.mockReset();

    // Instantiate the service directly with the mock HttpClient
    service = new UserService(mockedHttpClient);

    // Assign the mock for assertion checks
    httpClient = httpClientMock as unknown as jest.Mocked<HttpClient>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Jest assertion
  });

  it('should fetch user details by ID', (done) => {
    const userId = '1';
    // Adjust mockUser to match the User interface structure
    const mockUser: User = {
        id: 1, // Assuming id is number based on interface
        email: 'john.doe@example.com',
        lastName: 'Doe',
        firstName: 'John',
        admin: false,
        password: '', // Password might not be returned by API, adjust if needed
        createdAt: new Date()
        // updatedAt might be optional
    };

    // Configure the mock response for get
    httpClient.get.mockReturnValue(of(mockUser));

    service.getById(userId).subscribe((user) => {
      expect(user).toEqual(mockUser); // Jest assertion
      // Verify the mock was called correctly
      expect(httpClient.get).toHaveBeenCalledWith(`${apiPath}/${userId}`);
      expect(httpClient.get).toHaveBeenCalledTimes(1);
      done(); // Signal async test completion
    });
  });

  it('should delete a user by ID', (done) => {
    const userId = '1';
    const mockResponse = {}; // Or null, or whatever the API actually returns on successful delete

    // Configure the mock response for delete
    httpClient.delete.mockReturnValue(of(mockResponse));

    service.delete(userId).subscribe((response) => {
      // Adjust expectation based on actual API response for delete
      expect(response).toEqual(mockResponse);
      // Verify the mock was called correctly
      expect(httpClient.delete).toHaveBeenCalledWith(`${apiPath}/${userId}`);
      expect(httpClient.delete).toHaveBeenCalledTimes(1);
      done(); // Signal async test completion
    });
  });

  // --- Error Handling Tests ---

  it('should handle error when fetching user details by ID', (done) => {
    const userId = '1';
    const errorResponse = new HttpErrorResponse({
        error: 'User not found',
        status: 404, statusText: 'Not Found'
    });

    // Configure the mock to return an error Observable
    httpClient.get.mockReturnValue(throwError(() => errorResponse));

    service.getById(userId).subscribe({
        next: () => done.fail('should have failed with an error'),
        error: (error) => {
            expect(error).toBe(errorResponse);
            expect(httpClient.get).toHaveBeenCalledWith(`${apiPath}/${userId}`);
            expect(httpClient.get).toHaveBeenCalledTimes(1);
            done();
        }
    });
  });

  it('should handle error when deleting a user by ID', (done) => {
    const userId = '1';
    const errorResponse = new HttpErrorResponse({
        error: 'Deletion failed',
        status: 500, statusText: 'Internal Server Error'
    });

    // Configure the mock to return an error Observable
    httpClient.delete.mockReturnValue(throwError(() => errorResponse));

    service.delete(userId).subscribe({
        next: () => done.fail('should have failed with an error'),
        error: (error) => {
            expect(error).toBe(errorResponse);
            expect(httpClient.delete).toHaveBeenCalledWith(`${apiPath}/${userId}`);
            expect(httpClient.delete).toHaveBeenCalledTimes(1);
            done();
        }
    });
  });
});