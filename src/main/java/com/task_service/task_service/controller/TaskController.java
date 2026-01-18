package com.task_service.task_service.controller;

import com.task_service.task_service.dto.CreateTaskRequest;
import com.task_service.task_service.dto.UpdateTaskRequest;
import com.task_service.task_service.entity.Task;
import com.task_service.task_service.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /* ================= CREATE ================= */

    @PostMapping
    @PreAuthorize("hasAuthority('TASK_CREATE')")
    public Task createTask(@Valid @RequestBody CreateTaskRequest request) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignedTo(request.getAssignedTo());

        return taskService.createTask(task);
    }

    /* ================= UPDATE ================= */

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    public Task updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setAssignedTo(request.getAssignedTo());

        return taskService.updateTask(id, task);
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TASK_DELETE')")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /* ================= READ ================= */

    @GetMapping
    @PreAuthorize("hasAuthority('TASK_VIEW')")
    public List<Task> getMyTasks() {
        return taskService.getMyTasks();
    }
}
