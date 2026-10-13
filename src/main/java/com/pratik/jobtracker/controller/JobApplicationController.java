package com.pratik.jobtracker.controller;

import com.pratik.jobtracker.dto.CreateApplicationRequest;
import com.pratik.jobtracker.dto.UpdateApplicationStatusRequest;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication create(
            @Valid @RequestBody CreateApplicationRequest request) {

        return service.create(request);
    }
    @GetMapping("/{id}")
    public JobApplication getById(@PathVariable("id") long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<JobApplication> getAll(@RequestParam(name="status",required=false) ApplicationStatus status) {
        return service.getAll(status);
    }

    @PatchMapping("/{id}/status")
    public JobApplication updateStatus(
            @PathVariable("id") long id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return service.updateStatus(id,request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        service.delete(id);
    }
}