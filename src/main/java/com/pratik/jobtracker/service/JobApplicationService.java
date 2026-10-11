package com.pratik.jobtracker.service;


import com.pratik.jobtracker.dto.CreateApplicationRequest;
import com.pratik.jobtracker.dto.UpdateApplicationStatusRequest;
import com.pratik.jobtracker.exception.ApplicationNotFoundException;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobApplicationService {
    private final List<JobApplication> applications = new ArrayList<>();
    private long nextId = 1;

    public synchronized JobApplication create(CreateApplicationRequest request) {
        JobApplication application = new JobApplication();
        application.setId(nextId++);
        application.setCompany(request.company().strip());
        application.setRole(request.role().strip());
        application.setStatus(ApplicationStatus.SAVED);
        applications.add(application);
        return application;
    }

    public synchronized JobApplication getById(long id){
        for(JobApplication application : applications){
            if(application.getId() == id){
                return application;
            }
        }
        throw new ApplicationNotFoundException(id);
    }

    public synchronized List<JobApplication> getAll() {
        return new  ArrayList<>(applications);
    }


    public synchronized JobApplication updateStatus(long id ,ApplicationStatus status) {
        JobApplication application = getById(id);
        application.setStatus(status);
        return application;
    }
}
