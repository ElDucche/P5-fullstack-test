describe('Account spec', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: 'Dupont',
          firstName: 'Jean',
          createdAt: '2025-05-04T10:00:00.000Z',
          updatedAt: '2025-05-04T10:00:00.000Z',
        },
        {
          id: 2,
          lastName: 'Martin',
          firstName: 'Marie',
          createdAt: '2025-05-04T10:00:00.000Z',
          updatedAt: '2025-05-04T10:00:00.000Z',
        },
      ],
    });
    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session de Test',
          date: '2025-06-15T09:00:00.000Z',
          description: 'Description de la session de test.',
          teacher: {
            id: 1,
            lastName: 'Dupont',
            firstName: 'Jean',
            createdAt: '2025-05-04T10:00:00.000Z',
            updatedAt: '2025-05-04T10:00:00.000Z',
          },
          users: [
            {
              id: 1,
              email: 'utilisateur@test.com',
              lastName: 'Test',
              firstName: 'Utilisateur',
              admin: false,
              createdAt: '2025-05-04T11:00:00.000Z',
              updatedAt: '2025-05-04T11:00:00.000Z',
            },
          ],
          createdAt: '2025-05-04T12:00:00.000Z',
          updatedAt: '2025-05-04T12:00:00.000Z',
        },
      ],
    });
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });
    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: {
        message: 'Session created successfully',
      },
    }).as('createSession');
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session de Test',
        date: '2025-06-15T09:00:00.000Z',
        description: 'Description de la session de test.',
        teacher: {
          id: 1,
          lastName: 'Dupont',
          firstName: 'Jean',
          createdAt: '2025-05-04T10:00:00.000Z',
          updatedAt: '2025-05-04T10:00:00.000Z',
        },
        users: [
          {
            id: 1,
            email: 'user@test.com',
            lastName: 'Test',
            firstName: 'Utilisateur',
            admin: false,
            createdAt: '2025-05-04T11:00:00.000Z',
            updatedAt: '2025-05-04T11:00:00.000Z',
          },
        ],
        createdAt: '2025-05-04T12:00:00.000Z',
        updatedAt: '2025-05-04T12:00:00.000Z',
      },
    });
    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
      body: {
        message: 'Session deleted successfully',
      },
    }).as('deleteSession');
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: {
        message: 'Session updated successfully',
      },
    }).as('updateSession');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );
  });
});
