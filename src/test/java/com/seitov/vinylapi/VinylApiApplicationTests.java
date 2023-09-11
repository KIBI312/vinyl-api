package com.seitov.vinylapi;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class VinylApiApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = DatabaseContainer.getInstance();

	@Test
	void contextLoads() {
	}

}
