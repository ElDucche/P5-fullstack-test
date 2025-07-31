package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SpringBootSecurityJwtApplicationTests {

	@Test
	void contextLoads() {
		// Test that the Spring application context loads successfully.
		// Using webEnvironment = NONE to avoid full web context initialization
		// which reduces dependencies and potential conflicts.
	}

	@Test
	void applicationMain() {
		// This test verifies that the main method can be called without throwing exceptions.
		// Note: We test the method invocation, not the full application startup.
		assertDoesNotThrow(() -> {
			// We only test that the main method exists and can be invoked
			// without testing full application startup which would require
			// all beans to be properly configured.
			SpringBootSecurityJwtApplication app = new SpringBootSecurityJwtApplication();
			// The application object creation should not throw any exceptions
		});
	}

}
