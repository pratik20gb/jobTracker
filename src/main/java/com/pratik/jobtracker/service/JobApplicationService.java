package com.pratik.jobtracker.service;

import com.pratik.jobtracker.dto.CreateApplicationRequest;
import com.pratik.jobtracker.exception.ApplicationNotFoundException;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.repository.JobApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public JobApplication create(CreateApplicationRequest request) {
        JobApplication application = new JobApplication();

        application.setCompany(request.company().strip());
        application.setRole(request.role().strip());
        application.setStatus(ApplicationStatus.SAVED);

        return repository.save(application);
    }

    public Page<JobApplication> getAll(
            ApplicationStatus status,
            Pageable pageable
    ) {
        if (status == null) {
            return repository.findAll(pageable);
        }

        return repository.findByStatus(status, pageable);
    }

    public JobApplication getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    @Transactional
    public JobApplication updateStatus(long id, ApplicationStatus status) {
        JobApplication application = getById(id);

        application.setStatus(status);

        return application;
    }

    @Transactional
    public void delete(long id) {
        JobApplication application = getById(id);

        repository.delete(application);
    }
}