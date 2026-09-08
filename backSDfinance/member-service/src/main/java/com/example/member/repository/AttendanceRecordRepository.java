package com.example.member.repository;

import com.example.member.model.AttendanceRecord;
import com.example.member.model.StatutPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findBySessionId(Long sessionId);

    Optional<AttendanceRecord> findBySessionIdAndMemberId(Long sessionId, Long memberId);

    long countBySessionId(Long sessionId);

    long countBySessionIdAndStatut(Long sessionId, StatutPresence statut);

    List<AttendanceRecord> findByMemberId(Long memberId);

    void deleteBySessionId(Long sessionId);
}
