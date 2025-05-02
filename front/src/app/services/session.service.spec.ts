import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with isLogged as false', () => {
    expect(service.isLogged).toBeFalse();
  });

  it('should emit the initial isLogged value as false', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalse();
      done();
    });
  });

  it('should log in a user and update isLogged to true', () => {
    const mockUser: SessionInformation = { username: 'testUser', token: '12345' };
    service.logIn(mockUser);

    expect(service.isLogged).toBeTrue();
    expect(service.sessionInformation).toEqual(mockUser);
  });

  it('should emit true after logging in', (done) => {
    const mockUser: SessionInformation = { username: 'testUser', token: '12345' };
    service.logIn(mockUser);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeTrue();
      done();
    });
  });

  it('should log out a user and update isLogged to false', () => {
    service.logOut();

    expect(service.isLogged).toBeFalse();
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit false after logging out', (done) => {
    service.logOut();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalse();
      done();
    });
  });
});
