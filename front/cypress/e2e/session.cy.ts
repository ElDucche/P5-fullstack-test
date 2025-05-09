describe('Session spec ', () => {
    beforeEach(() => {
        
            })
    it('Session successfull', () => {
        cy.visit('/login')
        cy.intercept('GET', '/api/session', {
            body: [
                {
                  "id": 1,
                  "name": "Session de Test",
                  "date": "2025-06-15T09:00:00.000Z",
                  "description": "Description de la session de test.",
                  "teacher": {
                    "id": 1,
                    "lastName": "Dupont",
                    "firstName": "Jean",
                    "createdAt": "2025-05-04T10:00:00.000Z",
                    "updatedAt": "2025-05-04T10:00:00.000Z"
                  },
                  "users": [
                    {
                      "id": 1,
                      "email": "utilisateur@test.com",
                      "lastName": "Test",
                      "firstName": "Utilisateur",
                      "admin": false,
                      "createdAt": "2025-05-04T11:00:00.000Z",
                      "updatedAt": "2025-05-04T11:00:00.000Z"
                    }
                  ],
                  "createdAt": "2025-05-04T12:00:00.000Z",
                  "updatedAt": "2025-05-04T12:00:00.000Z"
                }
              ],
        })
            cy.intercept('POST', '/api/auth/login', {
                body: {
                  id: 1,
                  username: 'userName',
                  firstName: 'firstName',
                  lastName: 'lastName',
                  admin: true
                },
              })
              cy.get('input[formControlName=email]').type("yoga@studio.com")
              cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
    
        cy.get('.item').should('exist')
        cy.get('.items').should('have.length', 1)
    })
    // it('Session failed', () => {
    //     cy.url().should('include', '/sessions')
    
    //     cy.intercept('GET', '/api/session', {
    //         statusCode: 401,
    //         body: {
    //             message: 'Session failed'
    //         },
    //     })
    
    //     cy.get('.error').should('contain', 'An error occurred')
    //     cy.url().should('include', '/login')
    // })
})