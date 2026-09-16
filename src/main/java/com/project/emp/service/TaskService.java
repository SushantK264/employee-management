package com.project.emp.service;

import com.project.emp.dto.TaskDto;
import com.project.emp.entity.Task;
import com.project.emp.entity.User;
import com.project.emp.repository.TaskRepository;
import com.project.emp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDto createTask(TaskDto taskDto) {
        logger.info("Assigning new task to User ID: {}", taskDto.getAssignedUserId());
        
        User user = userRepository.findById(taskDto.getAssignedUserId())
                .orElseThrow(() -> {
                    logger.error("Cannot assign task. User not found with ID: {}", taskDto.getAssignedUserId());
                    return new RuntimeException("User not found");
                });

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus("PENDING");
        task.setAssignedUser(user);

        Task savedTask = taskRepository.save(task);
        logger.info("Task assigned successfully with Task ID: {}", savedTask.getId());
        
        taskDto.setId(savedTask.getId());
        taskDto.setStatus(savedTask.getStatus());
        
        return taskDto;
    }

    public Page<TaskDto> getTasksByUser(Long userId, int page, int size) {
        logger.info("Fetching tasks for User ID: {} | Page: {} | Size: {}", userId, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskRepository.findByAssignedUserId(userId, pageable);
        
        return taskPage.map(task -> {
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setDueDate(task.getDueDate());
            dto.setStatus(task.getStatus());
            dto.setAssignedUserId(task.getAssignedUser().getId());
            return dto;
        });
    }
    
    public TaskDto updateTaskStatus(Long taskId, String status) {
        logger.info("Updating Task ID: {} to status: {}", taskId, status);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    logger.error("Cannot update status. Task not found with ID: {}", taskId);
                    return new RuntimeException("Task not found");
                });

        task.setStatus(status);
        Task savedTask = taskRepository.save(task);
        logger.info("Task ID: {} status updated successfully", savedTask.getId());

        TaskDto dto = new TaskDto();
        dto.setId(savedTask.getId());
        dto.setTitle(savedTask.getTitle());
        dto.setDescription(savedTask.getDescription());
        dto.setDueDate(savedTask.getDueDate());
        dto.setStatus(savedTask.getStatus());
        dto.setAssignedUserId(savedTask.getAssignedUser().getId());
        
        return dto;
    }
}