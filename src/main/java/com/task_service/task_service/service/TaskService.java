package com.task_service.task_service.service;

import com.task_service.task_service.entity.Task;
import com.task_service.task_service.repository.TaskRepository;
import com.task_service.task_service.security.JwtContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final JwtContext jwtContext;

    /* ================= CREATE ================= */

    public Task createTask(Task task) {

        requirePermission("TASK_CREATE");

        Long userId = jwtContext.getUserId();

        task.setCreatedBy(userId);
        task.setAssignedTo(task.getAssignedTo()); // optional
        task.setStatus("NEW");

        return taskRepository.save(task);
    }

    /* ================= UPDATE ================= */

    public Task updateTask(Long taskId, Task updatedTask) {

        requirePermission("TASK_UPDATE");

        Task existing = getTaskOrThrow(taskId);
        requireOwnership(existing);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        existing.setAssignedTo(updatedTask.getAssignedTo());

        return taskRepository.save(existing);
    }

    /* ================= DELETE ================= */

    public void deleteTask(Long taskId) {

        requirePermission("TASK_DELETE");

        Task task = getTaskOrThrow(taskId);
        requireOwnership(task);

        taskRepository.delete(task);
    }

    /* ================= READ ================= */

    public List<Task> getMyTasks() {

        requirePermission("TASK_VIEW");

        Long userId = jwtContext.getUserId();

        if (jwtContext.isSuperAdmin()) {
            return taskRepository.findAll();
        }

        return taskRepository.findByCreatedBy(userId);
    }

    /* ================= HELPERS ================= */

    private Task getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    private void requirePermission(String permission) {
        if (!jwtContext.hasPermission(permission)) {
            throw new RuntimeException("Access denied: missing permission " + permission);
        }
    }

    private void requireOwnership(Task task) {

        if (jwtContext.isSuperAdmin()) {
            return;
        }

        Long userId = jwtContext.getUserId();

        boolean isOwner = task.getCreatedBy().equals(userId);
        boolean isAssignee = task.getAssignedTo() != null
                && task.getAssignedTo().equals(userId);

        if (!isOwner && !isAssignee) {
            throw new RuntimeException("Access denied: not task owner");
        }
    }
}
