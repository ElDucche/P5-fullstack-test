describe('Session spec ', () => {
    beforeEach(() => {
        
        cy.visit('/login')
        cy.intercept('GET', '/api/teacher', {
            body: [
                {
                  "id": 1,
                  "lastName": "Dupont",
                  "firstName": "Jean",
                  "createdAt": "2025-05-04T10:00:00.000Z",
                  "updatedAt": "2025-05-04T10:00:00.000Z"
                },
                {
                  "id": 2,
                  "lastName": "Martin",
                  "firstName": "Marie",
                  "createdAt": "2025-05-04T10:00:00.000Z",
                  "updatedAt": "2025-05-04T10:00:00.000Z"
                }
              ]
        })
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
        cy.intercept('POST', '/api/session', {
            statusCode: 200,
            body: {
                message: 'Session created successfully'
            }
        }).as('createSession')
        cy.intercept('GET', '/api/session/1', {
            body: {
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
                    "email": "user@test.com",
                    "lastName": "Test",
                    "firstName": "Utilisateur",
                    "admin": false,
                    "createdAt": "2025-05-04T11:00:00.000Z",
                    "updatedAt": "2025-05-04T11:00:00.000Z"
                    }
                ],
                "createdAt": "2025-05-04T12:00:00.000Z",
                "updatedAt": "2025-05-04T12:00:00.000Z"
                },
        })
        cy.intercept('DELETE', '/api/session/1', {
            statusCode: 200,
            body: {
                message: 'Session deleted successfully'
        }
        }).as('deleteSession')
        cy.intercept('PUT', '/api/session/1', {
            statusCode: 200,
            body: {
                message: 'Session updated successfully'
            }
        }).as('updateSession')
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    })
    it('Session successfull, User admin', () => {
    
    
        cy.get('mat-card-header').should('contain', 'Create')
        cy.get('.item').should('exist')
        cy.get('.items').should('have.length', 1)
        // I should see 2 buttons inside .item wich have span with text "Detail" and "Edit"
        cy.get('.item').should('contain', 'Detail')
        cy.get('.item').should('contain', 'Edit')

        cy.get('mat-card-header>button').click()
        cy.url().should('include', '/sessions/create')
        cy.get('h1').should('contain', 'Create session')
        cy.get('input[formControlName=name]').type("Session de Test")
        cy.get('input[formControlName=date]').type("2025-06-15")
        cy.get('textarea[formControlName=description]').type("Description de la session de test.")
        // Choose a teacher from the select list
        cy.get('mat-select[formControlName=teacher_id]').click()
        cy.get('mat-option').contains('Jean Dupont').click()
        cy.get('mat-select[formControlName=teacher_id]').should('contain', 'Jean Dupont')
       
        cy.get('button[type=submit]').click()
        cy.wait('@createSession').should((xhr) => {
            expect(xhr.response.statusCode).to.equal(200)
            expect(xhr.response.body.message).to.equal('Session created successfully')
        }
        )
        cy.url().should('include', '/sessions')
        cy.get('.item').should('exist')
        cy.get('.items').should('have.length', 1)
        cy.get('snack-bar-container').should('contain', 'Session created !')

        // I need to click on Detail button
        
                
        cy.get('.item').should('contain', 'Detail')
        cy.get('.item').contains('Detail').click()
        cy.url().should('include', '/sessions/detail/1')

       
        cy.get('mat-card-title').should('contain', 'Delete')
        cy.get('button').contains('Delete').click()
        cy.wait('@deleteSession').should((xhr) => {
            expect(xhr.response.statusCode).to.equal(200)
            expect(xhr.response.body.message).to.equal('Session deleted successfully')
        }
        )
        cy.url().should('include', '/sessions')
        cy.get('.item').should('exist')
        cy.get('.items').should('have.length', 1)
        cy.get('snack-bar-container').should('contain', 'Session deleted !')

        cy.get('.item').contains('Edit').click()
        cy.url().should('include', '/sessions/update/1')
        cy.get('h1').should('contain', 'Update session')
        cy.get('input[formControlName=name]').should('have.value', 'Session de Test')
        cy.get('input[formControlName=date]').should('have.value', '2025-06-15')
        cy.get('textarea[formControlName=description]').should('have.value', 'Description de la session de test.')
        cy.get('mat-select[formControlName=teacher_id]').as('teacherSelect').should('contain', '')
        cy.get('@teacherSelect').click()
        cy.get('mat-option').contains('Marie Martin').click()
        cy.get('@teacherSelect').should('contain', 'Marie Martin')
        cy.get('input[formControlName=name]').clear().type("Session de Test modifiée")
        cy.get('input[formControlName=date]').clear().type("2025-06-16")
        cy.get('textarea[formControlName=description]').clear().type("Description de la session de test modifiée.")
        cy.get('button[type=submit]').click()
        cy.wait('@updateSession').should((xhr) => {
            expect(xhr.response.statusCode).to.equal(200)
            expect(xhr.response.body.message).to.equal('Session updated successfully')
        }
        )
        cy.url().should('include', '/sessions')
        cy.get('.item').should('exist')
        cy.get('.items').should('have.length', 1)
        cy.get('snack-bar-container').should('contain', 'Session updated !')
        
    })
})

describe('Session spec ', () => {
    before(() => {
        cy.visit('/login')
        cy.intercept('GET', '/api/teacher', {
            body: [
                {
                  "id": 1,
                  "lastName": "Dupont",
                  "firstName": "Jean",
                  "createdAt": "2025-05-04T10:00:00.000Z",
                  "updatedAt": "2025-05-04T10:00:00.000Z"
                },
                {
                  "id": 2,
                  "lastName": "Martin",
                  "firstName": "Marie",
                  "createdAt": "2025-05-04T10:00:00.000Z",
                  "updatedAt": "2025-05-04T10:00:00.000Z"
                }
              ],})
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
                      "email": "notadmin@test.com",
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
              ]
        })
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: false
            },
        })
        cy.intercept('GET', '/api/session/1', {
            body: {
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
                  1
                ],
                "createdAt": "2025-05-04T12:00:00.000Z",
                "updatedAt": "2025-05-04T12:00:00.000Z"
              }
        })
        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200,
            body: {
                message: 'You are now registered for this session !'
            }
        }).as('participateSession')
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
            })
before(() => {
  cy.intercept(
            'GET',
            '/api/session/1',
            {
          body: {
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
              "users": [],
              "createdAt": "2025-05-04T12:00:00.000Z",
              "updatedAt": "2025-05-04T12:00:00.000Z"
            }
            }
        )
    })
    it('Session successfull, User not admin', () => {
        cy.url().should('include', '/sessions')
        cy.get('mat-card-header').should('not.contain', 'Create')

        cy.get('.item').should('contain', 'Detail')
        cy.get('.item').should('not.contain', 'Edit')
        cy.get('.item').contains('Detail').click()
        cy.url().should('include', '/sessions/detail/1')

        cy.intercept(
            'GET',
            '/api/session/1',
            {
          body: {
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
              "users": [],
              "createdAt": "2025-05-04T12:00:00.000Z",
              "updatedAt": "2025-05-04T12:00:00.000Z"
            }
            }
        )
        cy.get('mat-card-title').should('contain', 'Participate')
        cy.get('button').contains('Participate').click()
        cy.wait('@participateSession').should((xhr) => {
            expect(xhr.response.statusCode).to.equal(200)
            expect(xhr.response.body.message).to.equal('You are now registered for this session !')
        })
    })
})