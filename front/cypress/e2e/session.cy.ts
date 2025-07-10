describe('Session spec', () => {
  // --- ADMIN CONTEXT ---
  context('As an admin user', () => {
    beforeEach(() => {
      cy.fixture('admin.json').as('admin');
      cy.fixture('session.json').as('session');
      cy.fixture('teacher.json').as('teacher');

      cy.get('@admin').then((admin) => {
        cy.intercept('POST', '/api/auth/login', { body: admin }).as('login');
      });

      cy.intercept('GET', '/api/teacher', { fixture: 'teacher.json' }).as(
        'getTeachers'
      );
      cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' }).as(
        'getTeacher1'
      );
    });

    it('should be able to create, update, and delete a session', () => {
      // Initial state: no sessions
      cy.intercept('GET', '/api/session', { body: [] }).as(
        'getSessionsInitial'
      );

      // Login
      cy.visit('/login');
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);
      cy.url().should('include', '/sessions');
      cy.wait('@getSessionsInitial');

      // 1. Create
      cy.get('@session').then((session) => {
        cy.intercept('POST', '/api/session', { body: session }).as(
          'createSession'
        );
        cy.contains('button', 'Create').click();
        cy.url().should('include', '/sessions/create');

        cy.wait('@getTeachers'); // Wait for teachers to be loaded
        cy.get('input[formControlName=name]').type((session as any).name);
        cy.get('input[formControlName=date]').type('2025-07-20');
        cy.get('mat-select[formControlName=teacher_id]')
          .click()
          .get('mat-option')
          .contains('Jean Dupont')
          .click();
        cy.get('textarea[formControlName=description]').type(
          (session as any).description
        );
        cy.get('@session').then((session) => {
          cy.intercept('GET', '/api/session', { body: [session] }).as(
            'getSessionsAfterCreate'
          );
          cy.intercept('GET', '/api/session/1', { body: session }).as(
            'getSessionDetail'
          );
          const updatedSession = { ...session, name: 'Updated Session Name' };
          cy.intercept('PUT', '/api/session/1', { body: updatedSession }).as(
            'updateSession'
          );
        });
        cy.contains('button', 'Save').should('not.be.disabled').click();
      });

      cy.wait('@createSession');
      cy.get('snack-bar-container').should('contain', 'Session created !');

      // 2. Read and Update

      cy.url().should('include', '/sessions');
      cy.wait('@getSessionsAfterCreate');
      cy.contains('button', 'Edit').first().click();

      cy.url().should('include', '/sessions/update/1');
      cy.get('input[formControlName=name]')
        .clear()
        .type('Updated Session Name');
      cy.contains('button', 'Save').click();
      cy.wait('@updateSession');
      cy.get('snack-bar-container').should('contain', 'Session updated !');

      //   // 3. Delete
      cy.wait('@getSessionDetail');
      cy.contains('button', 'Detail').first().click();
      cy.url().should('include', '/sessions/detail/1');

      // cy.wait('@getSessionsBeforeDelete');

      cy.get('@session').then((session) => {
        cy.intercept('DELETE', '/api/session/1', { statusCode: 200 }).as(
          'deleteSession'
        );
        cy.intercept('GET', '/api/session', { body: [session] }).as(
          'getSessionsBeforeDelete'
        );
        cy.contains('button', 'Delete').click();
        cy.wait('@deleteSession');
      });

      cy.get('snack-bar-container').should('contain', 'Session deleted !');
      cy.url().should('include', '/sessions');
    });
  });

  // --- NOT SUBSCRIBED USER CONTEXT ---
  context('As a non-subscribed user', () => {
    beforeEach(() => {
      cy.fixture('user.json').as('user');
      cy.fixture('session.json').as('session');

      cy.get('@user').then((user) => {
        cy.intercept('POST', '/api/auth/login', { body: user }).as('login');
        cy.intercept(
          'POST',
          `/api/session/1/participate/${(user as any).id}`,
          {}
        ).as('participate');
      });

      cy.get('@session').then((session) => {
        // The user is NOT part of the session's users array initially
        cy.intercept('GET', '/api/session', { body: [session] }).as(
          'getSessions'
        );
        // Initial fetch
        cy.intercept('GET', '/api/session/1', { body: session }).as(
          'getSessionDetail'
        );
      });

      cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' }).as(
        'getTeacher'
      );

      // Login
      cy.visit('/login');
      cy.get('input[formControlName=email]').type('user@studio.com');
      cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);
      cy.url().should('include', '/sessions');
      cy.wait('@getSessions');
    });

    it('should be able to subscribe to a session and see the button change', () => {
      cy.contains('button', 'Detail').click();
      cy.wait('@getSessionDetail');
      cy.wait('@getTeacher');
      cy.url().should('include', '/sessions/detail/1');

      // Set up the intercept for the refresh call just before the action that triggers it.
      cy.get('@session').then((session) => {
        cy.get('@user').then((user) => {
          const updatedSession = { ...session, users: [(user as any).id] };
          cy.intercept('GET', '/api/session/1', { body: updatedSession }).as(
            'getSessionAfterParticipate'
          );
        });
      });

      cy.contains('button', 'Participate').should('not.be.disabled').click();

      cy.wait('@participate');
      cy.wait('@getSessionAfterParticipate'); // Wait for the UI to refresh

      cy.contains('button', 'Do not participate').should('be.visible');
    });
  });

  // --- SUBSCRIBED USER CONTEXT ---
  context('As a subscribed user', () => {
    beforeEach(() => {
      cy.fixture('user.json').as('user');
      cy.fixture('session.json').as('session');

      cy.get('@user').then((user) => {
        cy.intercept('POST', '/api/auth/login', { body: user }).as('login');
        cy.intercept(
          'DELETE',
          `/api/session/1/participate/${(user as any).id}`,
          {}
        ).as('unParticipate');
      });

      // Modify the session fixture on the fly to include the user
      cy.fixture('session.json').then((session) => {
        cy.get('@user').then((user) => {
          const subscribedSession = { ...session, users: [(user as any).id] };
          cy.intercept('GET', '/api/session', { body: [subscribedSession] }).as(
            'getSessions'
          );
          cy.intercept('GET', '/api/session/1', { body: subscribedSession }).as(
            'getSessionDetail'
          );
        });
      });

      cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' }).as(
        'getTeacher'
      );

      // Login
      cy.visit('/login');
      cy.get('input[formControlName=email]').type('user@studio.com');
      cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);
      cy.url().should('include', '/sessions');
      cy.wait('@getSessions');
    });

    it('should be able to unsubscribe from a session and see the button change', () => {
      cy.contains('button', 'Detail').click();
      cy.url().should('include', '/sessions/detail/1');
      cy.wait('@getSessionDetail');
      cy.wait('@getTeacher');

      // Set up the intercept for the refresh call just before the action that triggers it.
      cy.get('@session').then((session) => {
        cy.intercept('GET', '/api/session/1', { body: session }).as(
          'getSessionAfterUnParticipate'
        );
      });

      cy.contains('button', 'Do not participate')
        .should('not.be.disabled')
        .click();

      cy.wait('@unParticipate');
      cy.wait('@getSessionAfterUnParticipate'); // Wait for the UI to refresh

      cy.contains('button', 'Participate').should('be.visible');
    });
  });
});

describe('AuthGuard', () => {
  it('should redirect an unauthenticated user to /login when trying to access /sessions', () => {
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});

describe('Not Found', () => {
  it('should display the not found page for a non-existent URL', () => {
    cy.visit('/a-route-that-does-not-exist');
    cy.get('h1').contains('Page not found !').should('be.visible');
  });
});
