package com.task_service.task_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String status; // NEW / IN_PROGRESS / DONE

    private Long assignedTo;
}
