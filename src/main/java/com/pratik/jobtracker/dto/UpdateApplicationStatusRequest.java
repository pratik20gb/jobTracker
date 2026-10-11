package com.pratik.jobtracker.dto;

import com.pratik.jobtracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusRequest(@NotNull(message = "Status is required")
ApplicationStatus status ) {


}
