package com.project.emp.controller;

import com.project.emp.dto.LeaveDto;
import com.project.emp.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    public ResponseEntity<LeaveDto> applyLeave(@Valid @RequestBody LeaveDto leaveDto) {
        LeaveDto appliedLeave = leaveService.applyLeave(leaveDto);
        return new ResponseEntity<>(appliedLeave, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LeaveDto>> getLeavesByUser(@PathVariable Long userId) {
        List<LeaveDto> leaves = leaveService.getLeavesByUser(userId);
        return ResponseEntity.ok(leaves);
    }
    
    @PutMapping("/{leaveId}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<LeaveDto> updateLeaveStatus(
            @PathVariable Long leaveId, 
            @RequestParam String status) {
        
        LeaveDto updatedLeave = leaveService.updateLeaveStatus(leaveId, status);
        return ResponseEntity.ok(updatedLeave);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<LeaveDto>> getPendingLeaves() {
        List<LeaveDto> pendingLeaves = leaveService.getPendingLeaves();
        return ResponseEntity.ok(pendingLeaves);
    }
}