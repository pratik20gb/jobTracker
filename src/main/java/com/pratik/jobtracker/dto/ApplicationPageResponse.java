package com.pratik.jobtracker.dto;

import com.pratik.jobtracker.model.JobApplication;

import java.util.List;

public record ApplicationPageResponse(
        List<JobApplication> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}