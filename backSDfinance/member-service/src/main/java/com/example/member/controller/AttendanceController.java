package com.example.member.controller;

import com.example.member.dto.*;
import com.example.member.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/sessions")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<AttendanceSessionDto> createSession(@RequestBody CreateAttendanceSessionRequest request) {
        return ResponseEntity.ok(attendanceService.createSession(request));
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'LEADER')")
    public ResponseEntity<List<AttendanceSessionDto>> getAllSessions() {
        return ResponseEntity.ok(attendanceService.getAllSessions());
    }

    @GetMapping("/sessions/{id}")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'LEADER')")
    public ResponseEntity<AttendanceSessionDto> getSessionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(attendanceService.getSessionById(id));
    }

    @GetMapping("/sessions/{id}/records")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'LEADER')")
    public ResponseEntity<List<AttendanceRecordDto>> getSessionRecords(@PathVariable("id") Long id) {
        return ResponseEntity.ok(attendanceService.getSessionRecords(id));
    }

    @PostMapping("/sessions/{id}/batch")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<List<AttendanceRecordDto>> submitBatchAttendance(
            @PathVariable("id") Long id,
            @RequestBody SubmitBatchAttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.submitBatchAttendance(id, request));
    }

    @PutMapping("/sessions/{id}/close")
    @PreAuthorize("hasAuthority('PRESIDENT')")
    public ResponseEntity<AttendanceSessionDto> closeSession(@PathVariable("id") Long id) {
        return ResponseEntity.ok(attendanceService.closeSession(id));
    }

    @GetMapping("/stats/group/{groupeId}")
    @PreAuthorize("hasAnyAuthority('PRESIDENT', 'LEADER')")
    public ResponseEntity<AttendanceStatsDto> getGroupAttendanceStats(@PathVariable("groupeId") Long groupeId) {
        return ResponseEntity.ok(attendanceService.getGroupAttendanceStats(groupeId));
    }
}
