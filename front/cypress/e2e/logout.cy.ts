describe('Logout spec', () => {
    it('Logout successfull', () => {
        cy.visit('/sessions')
    
        cy.intercept('POST', '/api/auth/logout', {
        statusCode: 200,
        body: {
            message: 'Logout successful'
        },
        })
    
        cy.intercept(
        {
            method: 'GET',
            url: '/api/session',
        },
        []).as('session')
    
        cy.get('.logout-button').click()
    
        cy.url().should('include', '/login')
    })
    it('Logout failed', () => {
        cy.visit('/sessions')
        
        cy.intercept('POST', '/api/auth/logout', {
            statusCode: 401,
            body: {
                message: 'Logout failed'
            },
        })
        
        cy.get('.logout-button').click()
        
        cy.get('.error').should('contain', 'An error occurred')
        cy.url().should('include', '/sessions')
    })
})
    