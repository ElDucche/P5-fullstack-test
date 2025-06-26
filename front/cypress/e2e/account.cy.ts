describe('Account spec', () => {
    beforeEach(() => {
        cy.visit('/login')
        

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })
        cy.intercept('GET', '/api/user/1', {
            body: [
                {
                    id: 1,
                    firstName: 'John',
                    lastName: 'Doe',
                    email: 'test@yoga.zyx',
                }]
        })
    })
})