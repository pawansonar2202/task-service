package com.task_service.task_service.repository;

import com.task_service.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // USER: view own tasks
    List<Task> findByCreatedBy(Long createdBy);

    // ADMIN: view assigned tasks
    List<Task> findByAssignedTo(Long assignedTo);
}
