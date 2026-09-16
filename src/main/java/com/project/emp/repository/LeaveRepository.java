package com.project.emp.repository;

import com.project.emp.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUserId(Long userId);
    List<Leave> findByStatus(String status);
}