import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals'; // Jest expect
import { of } from 'rxjs';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service'; // Import AuthService
import { SessionService } from './services/session.service'; // Import SessionService

// --- Mocks ---
// Mock SessionService
const mockSessionService = {
  sessionInformation: undefined, // Ou un mock SessionInformation si nécessaire
  isLogged: false,
  $isLogged: jest.fn().mockReturnValue(of(false)), // Mock l'observable
  logIn: jest.fn(),
  logOut: jest.fn(),
};

// Mock AuthService (même si non utilisé directement dans le template/code montré, il est injecté)
const mockAuthService = {
  // Ajoutez des mocks pour les méthodes utilisées si nécessaire
};

// Mock Router
const mockRouter = {
  navigate: jest.fn()
};

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService; // Pour accéder au mock injecté
  let router: Router; // Pour accéder au mock injecté

  beforeEach(async () => {
    // Réinitialiser les mocks avant chaque test
    mockSessionService.$isLogged.mockClear().mockReturnValue(of(false)); // Réinitialise et redéfinit la valeur par défaut
    mockSessionService.logOut.mockClear();
    mockRouter.navigate.mockClear();

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule, // Fournit des stubs pour les directives de routage comme router-outlet
        HttpClientModule, // Nécessaire si des services injectés l'utilisent
        MatToolbarModule // Importe le module utilisé dans le template
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        // Fournir les mocks pour les services injectés
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    // Injecter les mocks pour pouvoir les espionner ou vérifier leurs appels
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    // fixture.detectChanges(); // Déclencher la détection de changements si nécessaire (ex: pour le template)
  });

  it('should create the app', () => {
    expect(component).toBeTruthy(); // Jest assertion
  });

  it('should call sessionService.$isLogged when $isLogged is called', () => {
    component.$isLogged();
    expect(sessionService.$isLogged).toHaveBeenCalled(); // Vérifie si la méthode du mock a été appelée
  });

  it('should return the observable from sessionService.$isLogged', (done) => {
    // Configurer le mock pour retourner une valeur spécifique pour ce test si nécessaire
    mockSessionService.$isLogged.mockReturnValue(of(true));

    component.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true); // Vérifie la valeur émise par l'observable retourné
      done();
    });
  });

  it('should call sessionService.logOut and router.navigate on logout', () => {
    component.logout();
    expect(sessionService.logOut).toHaveBeenCalled(); // Vérifie l'appel à logOut sur le mock
    expect(router.navigate).toHaveBeenCalledWith(['']); // Vérifie l'appel à navigate avec le bon chemin
  });

  // Vous pouvez ajouter d'autres tests, par exemple pour vérifier le rendu du template
  // en fonction de l'état de connexion, en utilisant fixture.detectChanges() et
  // en interrogeant le DOM via fixture.nativeElement.
});