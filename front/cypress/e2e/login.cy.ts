describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: 'un-token-jwt-simule',
        type: 'bearer',
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
  it('Login failed', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        message: 'Invalid credentials'
      },
    })

    cy.get('input[formControlName=email]').type("unregistered@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('.error').should('contain', 'An error occurred')
    cy.url().should('include', '/login')
  })
  it('Login with empty fields', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: {
        message: 'An error occurred'
      },
    })

    cy.get('input[formControlName=email]').type(" ")
    cy.get('input[formControlName=password]').type(`${" "}{enter}{enter}`)
    cy.get('.error').should('contain', 'An error occurred')
    cy.url().should('include', '/login')
  })
});