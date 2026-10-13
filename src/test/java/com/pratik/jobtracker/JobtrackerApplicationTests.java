package com.pratik.jobtracker;

import com.pratik.jobtracker.dto.CreateApplicationRequest;
import com.pratik.jobtracker.exception.ApplicationNotFoundException;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(JobtrackerApplicationTests.DatabaseConfig.class)
class JobtrackerApplicationTests {

	@TestConfiguration(proxyBeanMethods = false)
	static class DatabaseConfig {

		@Bean
		@ServiceConnection
		PostgreSQLContainer postgres() {
			return new PostgreSQLContainer("postgres:17");
		}
	}

	@Autowired
	private JobApplicationService service;

	@Test
	void applicationCanBeCreatedUpdatedAndDeletedInDatabase() {
		JobApplication first = service.create(
				new CreateApplicationRequest("Company A", "Java Developer"));

		JobApplication second = service.create(
				new CreateApplicationRequest("Company B", "Backend Developer"));

		assertNotNull(first.getId());

		JobApplication loaded = service.getById(first.getId());
		assertEquals("Company A", loaded.getCompany());
		assertEquals(ApplicationStatus.SAVED, loaded.getStatus());

		service.updateStatus(first.getId(), ApplicationStatus.APPLIED);
		// Without a filter, both applications should be returned.
		assertEquals(2, service.getAll(null).size());

// APPLIED filter should return only the first application.
		var applied = service.getAll(ApplicationStatus.APPLIED);

		assertEquals(1, applied.size());
		assertEquals(first.getId(), applied.getFirst().getId());

// SAVED filter should return only the second application.
		var saved = service.getAll(ApplicationStatus.SAVED);

		assertEquals(1, saved.size());
		assertEquals(second.getId(), saved.getFirst().getId());

// A status with no matching applications should return an empty list.
		assertTrue(service.getAll(ApplicationStatus.OFFER).isEmpty());

		assertEquals(
				ApplicationStatus.APPLIED,
				service.getById(first.getId()).getStatus()
		);

		service.delete(first.getId());

		assertThrows(
				ApplicationNotFoundException.class,
				() -> service.getById(first.getId())
		);

		assertEquals(
				"Company B",
				service.getById(second.getId()).getCompany()
		);
	}
}