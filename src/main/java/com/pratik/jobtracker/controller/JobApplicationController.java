package com.pratik.jobtracker.controller;

import com.pratik.jobtracker.dto.ApplicationPageResponse;
import com.pratik.jobtracker.dto.CreateApplicationRequest;
import com.pratik.jobtracker.dto.UpdateApplicationStatusRequest;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ApplicationPageResponse getAll(
            @RequestParam(name = "status", required = false)
            ApplicationStatus status,

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "10")
            int size
    ) {
        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "page must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "size must be between 1 and 100"
            );
        }

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );

        var result = service.getAll(status, pageable);

        return new ApplicationPageResponse(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
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