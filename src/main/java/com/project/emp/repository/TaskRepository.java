package com.project.emp.repository;

import com.project.emp.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    Page<Task> findByAssignedUserId(Long userId, Pageable pageable); 
    
}