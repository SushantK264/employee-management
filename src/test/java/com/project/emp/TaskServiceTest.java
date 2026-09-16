package com.project.emp;

import com.project.emp.dto.TaskDto;
import com.project.emp.entity.Task;
import com.project.emp.entity.User;
import com.project.emp.repository.TaskRepository;
import com.project.emp.repository.UserRepository;
import com.project.emp.service.TaskService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testCreateTask_Success() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);

        TaskDto inputDto = new TaskDto();
        inputDto.setTitle("Implement Security");
        inputDto.setDescription("Add JWT filters");
        inputDto.setAssignedUserId(1L);

        Task savedTask = new Task();
        savedTask.setId(100L);
        savedTask.setTitle("Implement Security");
        savedTask.setStatus("PENDING");
        savedTask.setAssignedUser(mockUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        TaskDto result = taskService.createTask(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("PENDING", result.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
}