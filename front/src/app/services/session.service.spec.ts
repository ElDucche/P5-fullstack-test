// Remove Angular testing imports
// import { TestBed } from '@angular/core/testing';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService (Pure Jest)', () => {
  let service: SessionService;

  beforeEach(() => {
    // Instantiate the service directly
    service = new SessionService();
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Jest assertion
  });

  it('should initialize with isLogged as false', () => {
    expect(service.isLogged).toBeFalsy(); // Jest assertion
    expect(service.sessionInformation).toBeUndefined(); // Check initial state
  });

  it('should emit the initial isLogged value as false', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalsy(); // Jest assertion
      done();
    });
  });

  it('should log in a user and update isLogged to true and store session info', () => {
    // Use a more complete mock matching the interface
    const mockUser: SessionInformation = {
        token: 'mockToken123',
        type: 'bearer',
        id: 1,
        username: 'testUser',
        firstName: 'Test',
        lastName: 'User',
        admin: false
    };
    service.logIn(mockUser);

    expect(service.isLogged).toBeTruthy(); // Jest assertion
    expect(service.sessionInformation).toEqual(mockUser); // Check stored info
  });

  it('should emit true after logging in', (done) => {
     const mockUser: SessionInformation = {
        token: 'mockToken123',
        type: 'bearer',
        id: 1,
        username: 'testUser',
        firstName: 'Test',
        lastName: 'User',
        admin: false
    };
    // Log in first to change the state
    service.logIn(mockUser);

    // Subscribe to the observable to check the emitted value
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeTruthy(); // Jest assertion
      done();
    });
  });

  it('should log out a user and update isLogged to false and clear session info', () => {
     // First, log in a user to ensure state changes
     const mockUser: SessionInformation = {
        token: 'mockToken123',
        type: 'bearer',
        id: 1,
        username: 'testUser',
        firstName: 'Test',
        lastName: 'User',
        admin: false
    };
    service.logIn(mockUser);
    expect(service.isLogged).toBeTruthy(); // Verify logged in state

    // Then log out
    service.logOut();

    expect(service.isLogged).toBeFalsy(); // Jest assertion
    expect(service.sessionInformation).toBeUndefined(); // Check cleared info
  });

  it('should emit false after logging out', (done) => {
    // Log in first
     const mockUser: SessionInformation = {
        token: 'mockToken123',
        type: 'bearer',
        id: 1,
        username: 'testUser',
        firstName: 'Test',
        lastName: 'User',
        admin: false
    };
    service.logIn(mockUser);

    // Then log out
    service.logOut();

    // Subscribe to check the emitted value
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalsy(); // Jest assertion
      done();
    });
  });
});