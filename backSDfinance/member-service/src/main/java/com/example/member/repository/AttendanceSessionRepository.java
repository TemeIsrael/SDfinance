package com.example.member.repository;

import com.example.member.model.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    List<AttendanceSession> findByGroupeId(Long groupeId);

    List<AttendanceSession> findByGroupeIdIn(List<Long> groupeIds);
}
