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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
		var pageable = PageRequest.of(
				0, 10, Sort.by("id").ascending()
		);

// Without a filter, both applications are included.
		var all = service.getAll(null, pageable);

		assertEquals(2L, all.getTotalElements());
		assertEquals(2, all.getContent().size());

// Filtering still works.
		var applied = service.getAll(ApplicationStatus.APPLIED, pageable);

		assertEquals(1L, applied.getTotalElements());
		assertEquals(
				first.getId(),
				applied.getContent().getFirst().getId()
		);

		var saved = service.getAll(ApplicationStatus.SAVED, pageable);

		assertEquals(1L, saved.getTotalElements());
		assertEquals(
				second.getId(),
				saved.getContent().getFirst().getId()
		);

		assertTrue(
				service.getAll(ApplicationStatus.OFFER, pageable).isEmpty()
		);

// Page size 1 splits the two records into two pages.
		var firstPage = service.getAll(
				null,
				PageRequest.of(0, 1, Sort.by("id").ascending())
		);

		var secondPage = service.getAll(
				null,
				PageRequest.of(1, 1, Sort.by("id").ascending())
		);

		assertEquals(2L, firstPage.getTotalElements());
		assertEquals(2, firstPage.getTotalPages());
		assertEquals(1, firstPage.getContent().size());
		assertEquals(1, secondPage.getContent().size());

		assertEquals(
				first.getId(),
				firstPage.getContent().getFirst().getId()
		);
		assertEquals(
				second.getId(),
				secondPage.getContent().getFirst().getId()
		);

// A page beyond the results has empty content.
		assertTrue(
				service.getAll(
						null,
						PageRequest.of(2, 1, Sort.by("id").ascending())
				).isEmpty()
		);

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