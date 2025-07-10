describe('Register spec', () => {
    it('Register successfull', () => {
        cy.visit('/register')
    
        cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
        body: {
            message: 'Registration successful'
        },
        })
    
        cy.get('input[formControlName=email]').type("test@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.get('input[formControlName=firstName]').type("test")
        cy.get('input[formControlName=lastName]').type("test")

        cy.get('button[type=submit]').click()
        cy.url().should('include', '/login')
    })
    it('Register failed', () => {
        cy.visit('/register')
    
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 500,
            body: {
                message: 'Registration failed'
            },
        })
    
        cy.get('input[formControlName=email]').type("test@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.get('input[formControlName=firstName]').type("test")
        cy.get('input[formControlName=lastName]').type("test")

        cy.get('button[type=submit]').click()
        cy.get('.error').should('contain', 'An error occurred')
        cy.url().should('include', '/register')
    })
    it('Register with empty fields', () => {
        cy.visit('/register')
    
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                message: 'An error occurred'
            },
        })
    
        cy.get('input[formControlName=email]').type(" ")
        cy.get('input[formControlName=password]').type(`${" "}{enter}{enter}`)
        cy.get('input[formControlName=firstName]').type(" ")
        cy.get('input[formControlName=lastName]').type(" ")

        cy.get('button[type=submit]').should('be.disabled')
    })
    it('User credentials already exist', () => {
        cy.visit('/register')
    
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 409,
            body: {
                message: 'User already exists'
            },
        })
    
        cy.get('input[formControlName=email]').type("test@yoga.mail")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.get('input[formControlName=firstName]').type("test")
        cy.get('input[formControlName=lastName]').type("test")  
        cy.get('button[type=submit]').click()
        cy.get('.error').should('contain', 'An error occurred')
        cy.url().should('include', '/register')
    })
})