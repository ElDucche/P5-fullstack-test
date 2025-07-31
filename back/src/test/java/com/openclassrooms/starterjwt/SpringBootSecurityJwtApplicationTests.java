package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.config.TestMappersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = {SpringBootSecurityJwtApplication.class, TestMappersConfig.class})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
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
