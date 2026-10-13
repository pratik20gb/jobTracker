package com.pratik.jobtracker.repository;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Page<JobApplication> findByStatus(ApplicationStatus status, Pageable pageable);
}
