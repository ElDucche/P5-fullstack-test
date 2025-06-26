package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SpringBootSecurityJwtApplicationTests {

	@Test
	void contextLoads() {
		// Test that the Spring application context loads successfully.
	}

	@Test
	void applicationMain() {
		assertDoesNotThrow(() -> SpringBootSecurityJwtApplication.main(new String[]{}));
	}

}
