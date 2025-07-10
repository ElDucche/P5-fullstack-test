describe('User account page', () => {

  beforeEach(() => {
    // Login as a regular user first
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: 'jwt-token-for-user',
        type: 'bearer',
        id: 2,
        username: 'user@test.com',
        firstName: 'Regular',
        lastName: 'User',
        admin: false
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/user/2', {
      body: {
        id: 2,
        email: 'user@test.com',
        lastName: 'User',
        firstName: 'Regular',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date()
      },
    }).as('getUser');

    cy.get('input[formControlName=email]').type("user@test.com");
    cy.get('input[formControlName=password]').type("password");
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest');

    // Navigate to the account page
    cy.get('span.link').contains('Account').click();
    cy.wait('@getUser');
    cy.url().should('include', '/me');
  });

  it('should display user information correctly', () => {
    cy.get('p').should('contain', 'Name: Regular USER');
    cy.get('p').should('contain', 'Email: user@test.com');
    cy.get('p.my2').should('not.exist'); // Admin message should not be present
  });

  it('should allow a user to delete their account', () => {
    cy.intercept('DELETE', '/api/user/2', {
      statusCode: 200,
      body: {}
    }).as('deleteUser');

    cy.get('button[color="warn"]').contains('Delete').click();

    cy.wait('@deleteUser');

    cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
    cy.url().should('eq', 'http://localhost:4200/');
  });

  it('should not display the delete button for an admin user', () => {
    // The beforeEach logs in as a regular user. We need to log out first.
    cy.get('span.link').contains('Logout').click();
    cy.url().should('eq', 'http://localhost:4200/');

    // Now, login as admin
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: 'jwt-token-for-admin',
        type: 'bearer',
        id: 1,
        username: 'admin@test.com',
        firstName: 'Admin',
        lastName: 'User',
        admin: true
      },
    }).as('adminLogin');

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'admin@test.com',
        lastName: 'User',
        firstName: 'Admin',
        admin: true,
        createdAt: new Date(),
        updatedAt: new Date()
      },
    }).as('getAdmin');

    cy.get('input[formControlName=email]').type("admin@test.com");
    cy.get('input[formControlName=password]').type("password");
    cy.get('button[type="submit"]').click();
    cy.wait('@adminLogin');

    cy.url().should('include', '/sessions'); // Verify redirection after login

    cy.get('span.link').contains('Account').click();
    cy.wait('@getAdmin');

    cy.url().should('include', '/me');
    cy.get('p').should('contain', 'You are admin');
    cy.get('button[color="warn"]').should('not.exist');
  });

});
