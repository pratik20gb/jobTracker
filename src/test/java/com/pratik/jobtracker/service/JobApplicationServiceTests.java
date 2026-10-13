package com.pratik.jobtracker.service;

import com.pratik.jobtracker.exception.ApplicationNotFoundException;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.repository.JobApplicationRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JobApplicationServiceTests {

    @Test
    void deleteRemovesTheRequestedApplication() {
        JobApplicationRepository repository =
                mock(JobApplicationRepository.class);

        JobApplicationService service =
                new JobApplicationService(repository);

        JobApplication application = new JobApplication();
        application.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(application));

        service.delete(1L);

        verify(repository).delete(application);
    }

    @Test
    void deleteMissingApplicationDoesNotCallDelete() {
        JobApplicationRepository repository =
                mock(JobApplicationRepository.class);

        JobApplicationService service =
                new JobApplicationService(repository);

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ApplicationNotFoundException.class,
                () -> service.delete(999L)
        );

        verify(repository, never()).delete(any(JobApplication.class));
    }
}