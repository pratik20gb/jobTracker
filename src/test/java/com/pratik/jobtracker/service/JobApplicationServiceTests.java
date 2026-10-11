package com.pratik.jobtracker.service;

import com.pratik.jobtracker.dto.CreateApplicationRequest;
import com.pratik.jobtracker.exception.ApplicationNotFoundException;
import com.pratik.jobtracker.model.JobApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationServiceTests {

    @Test
    void deleteRemovesOnlyTheSelectedApplication() {
        JobApplicationService service = new JobApplicationService();

        JobApplication first = service.create(
                new CreateApplicationRequest("Company A", "Backend Developer"));

        JobApplication second = service.create(
                new CreateApplicationRequest("Company B", "Java Developer"));

        service.delete(first.getId());

        assertThrows(
                ApplicationNotFoundException.class,
                () -> service.getById(first.getId())
        );

        assertEquals(1, service.getAll().size());
        assertEquals(second.getId(), service.getAll().get(0).getId());
    }

    @Test
    void deleteMissingApplicationThrowsAndPreservesExistingData() {
        JobApplicationService service = new JobApplicationService();

        JobApplication existing = service.create(
                new CreateApplicationRequest("Company A", "Backend Developer"));

        assertThrows(
                ApplicationNotFoundException.class,
                () -> service.delete(existing.getId() + 1)
        );

        assertEquals(1, service.getAll().size());
        assertEquals(existing.getId(), service.getAll().get(0).getId());
    }
}