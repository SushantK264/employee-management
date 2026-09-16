package com.project.emp.service;

import com.project.emp.dto.LeaveDto;
import com.project.emp.entity.Leave;
import com.project.emp.entity.User;
import com.project.emp.repository.LeaveRepository;
import com.project.emp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LeaveService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);

    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;

    public LeaveService(LeaveRepository leaveRepository, UserRepository userRepository) {
        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
    }

    public LeaveDto applyLeave(LeaveDto leaveDto) {
        logger.info("User ID: {} is applying for leave", leaveDto.getUserId());
        
        User user = userRepository.findById(leaveDto.getUserId())
                .orElseThrow(() -> {
                    logger.error("Cannot apply leave. User not found with ID: {}", leaveDto.getUserId());
                    return new RuntimeException("User not found");
                });

        Leave leave = new Leave();
        leave.setStartDate(leaveDto.getStartDate());
        leave.setEndDate(leaveDto.getEndDate());
        leave.setReason(leaveDto.getReason());
        leave.setStatus("PENDING");
        leave.setUser(user);

        Leave savedLeave = leaveRepository.save(leave);
        logger.info("Leave request created successfully with ID: {}", savedLeave.getId());
        
        leaveDto.setId(savedLeave.getId());
        leaveDto.setStatus(savedLeave.getStatus());
        
        return leaveDto;
    }
    
    public List<LeaveDto> getLeavesByUser(Long userId) {
        logger.info("Fetching leave history for User ID: {}", userId);
        
        List<Leave> leaves = leaveRepository.findByUserId(userId);
        List<LeaveDto> leaveDtos = new ArrayList<>();
        
        for (Leave leave : leaves) {
            LeaveDto dto = new LeaveDto();
            dto.setId(leave.getId());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setReason(leave.getReason());
            dto.setStatus(leave.getStatus());
            dto.setUserId(leave.getUser().getId());
            leaveDtos.add(dto);
        }
        
        return leaveDtos;
    }

    public LeaveDto updateLeaveStatus(Long leaveId, String status) {
        logger.info("Updating status of Leave ID: {} to {}", leaveId, status);
        
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> {
                    logger.error("Cannot update status. Leave request not found with ID: {}", leaveId);
                    return new RuntimeException("Leave request not found");
                });
        
        leave.setStatus(status);
        Leave savedLeave = leaveRepository.save(leave);
        logger.info("Leave ID: {} status updated successfully", savedLeave.getId());
        
        LeaveDto dto = new LeaveDto();
        dto.setId(savedLeave.getId());
        dto.setStartDate(savedLeave.getStartDate());
        dto.setEndDate(savedLeave.getEndDate());
        dto.setReason(savedLeave.getReason());
        dto.setStatus(savedLeave.getStatus());
        dto.setUserId(savedLeave.getUser().getId());
        
        return dto;
    }

    public List<LeaveDto> getPendingLeaves() {
        logger.info("Fetching all pending leave requests for Manager/Admin review");
        
        List<Leave> pendingLeaves = leaveRepository.findByStatus("PENDING");
        List<LeaveDto> leaveDtos = new ArrayList<>();
        
        for (Leave leave : pendingLeaves) {
            LeaveDto dto = new LeaveDto();
            dto.setId(leave.getId());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setReason(leave.getReason());
            dto.setStatus(leave.getStatus());
            dto.setUserId(leave.getUser().getId());
            leaveDtos.add(dto);
        }
        
        return leaveDtos;
    }
}