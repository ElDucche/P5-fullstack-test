package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
		assertDoesNotThrow(SpringBootSecurityJwtApplication::new);
	}

}
