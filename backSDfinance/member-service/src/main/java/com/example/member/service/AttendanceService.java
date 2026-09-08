package com.example.member.service;

import com.example.common.model.UserRole;
import com.example.common.security.SecurityContextHelper;
import com.example.member.dto.*;
import com.example.member.model.*;
import com.example.member.repository.AttendanceRecordRepository;
import com.example.member.repository.AttendanceSessionRepository;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextHelper securityHelper;

    /**
     * Création d'une session d'appel.
     * Réservé au PRESIDENT (pour son propre groupe).
     * Initialise automatiquement les fiches de présence de tous les membres du groupe au statut NON_RENSEIGNE.
     */
    @Transactional
    public AttendanceSessionDto createSession(CreateAttendanceSessionRequest request) {
        securityHelper.requireRole(UserRole.PRESIDENT);
        securityHelper.requireGroupeAccess(request.getGroupeId());

        AttendanceSession session = AttendanceSession.builder()
                .titre(request.getTitre())
                .typeCulte(request.getTypeCulte() != null ? request.getTypeCulte() : TypeCulte.CULTE_DIMANCHE)
                .dateSession(request.getDateSession() != null ? request.getDateSession() : LocalDateTime.now())
                .groupeId(request.getGroupeId())
                .cloturee(false)
                .remarque(request.getRemarque())
                .libelle(request.getLibelle())
                .build();

        AttendanceSession savedSession = sessionRepository.save(session);

        // Récupérer tous les membres du groupe et initialiser leurs enregistrements avec le statut NON_RENSEIGNE
        List<Member> groupMembers = memberRepository.findByGroupeIdIn(Collections.singletonList(request.getGroupeId()));
        List<AttendanceRecord> initialRecords = groupMembers.stream()
                .map(m -> AttendanceRecord.builder()
                        .sessionId(savedSession.getId())
                        .memberId(m.getId())
                        .statut(StatutPresence.NON_RENSEIGNE)
                        .build())
                .collect(Collectors.toList());

        recordRepository.saveAll(initialRecords);

        return toSessionDto(savedSession);
    }

    /**
     * Récupérer la liste des sessions d'appel.
     * Accessible par le PRESIDENT (ses groupes) et le LEADER (tous les groupes).
     */
    public List<AttendanceSessionDto> getAllSessions() {
        UserRole role = securityHelper.getCurrentRole();
        List<AttendanceSession> sessions;

        if (role == UserRole.LEADER) {
            sessions = sessionRepository.findAll();
        } else if (role == UserRole.PRESIDENT) {
            sessions = sessionRepository.findByGroupeIdIn(securityHelper.getCurrentGroupIds());
        } else {
            throw new org.springframework.security.access.AccessDeniedException("Accès refusé aux sessions d'appel");
        }

        return sessions.stream().map(this::toSessionDto).collect(Collectors.toList());
    }

    /**
     * Récupérer une session par son ID avec vérification des droits.
     */
    public AttendanceSessionDto getSessionById(Long sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée : " + sessionId));

        securityHelper.requireGroupeAccess(session.getGroupeId());
        return toSessionDto(session);
    }

    /**
     * Récupérer les enregistrements de présence (feuille d'appel) d'une session.
     */
    public List<AttendanceRecordDto> getSessionRecords(Long sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée : " + sessionId));

        securityHelper.requireGroupeAccess(session.getGroupeId());

        List<AttendanceRecord> records = recordRepository.findBySessionId(sessionId);
        
        // Charger les informations des membres pour enrichir le DTO avec Nom & Prénom
        List<Long> memberIds = records.stream().map(AttendanceRecord::getMemberId).collect(Collectors.toList());
        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));

        return records.stream().map(r -> {
            Member m = memberMap.get(r.getMemberId());
            return AttendanceRecordDto.builder()
                    .id(r.getId())
                    .sessionId(r.getSessionId())
                    .memberId(r.getMemberId())
                    .memberNom(m != null ? m.getNom() : "")
                    .memberPrenom(m != null ? m.getPrenom() : "")
                    .statut(r.getStatut())
                    .heurePointage(r.getHeurePointage())
                    .remarque(r.getRemarque())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Soumettre l'appel en masse pour une session.
     * Réservé au PRESIDENT.
     */
    @Transactional
    public List<AttendanceRecordDto> submitBatchAttendance(Long sessionId, SubmitBatchAttendanceRequest request) {
        securityHelper.requireRole(UserRole.PRESIDENT);

        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée : " + sessionId));

        securityHelper.requireGroupeAccess(session.getGroupeId());

        if (session.isCloturee()) {
            throw new IllegalStateException("La session d'appel est clôturée. Impossible de modifier les présences.");
        }

        List<AttendanceRecord> existingRecords = recordRepository.findBySessionId(sessionId);
        Map<Long, AttendanceRecord> recordMap = existingRecords.stream()
                .collect(Collectors.toMap(AttendanceRecord::getMemberId, Function.identity()));

        LocalDateTime now = LocalDateTime.now();

        for (BatchAttendanceItemDto item : request.getItems()) {
            AttendanceRecord record = recordMap.get(item.getMemberId());
            if (record == null) {
                record = AttendanceRecord.builder()
                        .sessionId(sessionId)
                        .memberId(item.getMemberId())
                        .build();
            }

            if (item.getStatut() != null) {
                record.setStatut(item.getStatut());
                if (item.getStatut() == StatutPresence.PRESENT) {
                    record.setHeurePointage(now);
                }
            }
            if (item.getRemarque() != null) {
                record.setRemarque(item.getRemarque());
            }
            recordMap.put(item.getMemberId(), record);
        }

        List<AttendanceRecord> savedRecords = recordRepository.saveAll(recordMap.values());
        return getSessionRecords(sessionId);
    }

    /**
     * Clôturer une session d'appel.
     * Réservé au PRESIDENT.
     */
    @Transactional
    public AttendanceSessionDto closeSession(Long sessionId) {
        securityHelper.requireRole(UserRole.PRESIDENT);

        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée : " + sessionId));

        securityHelper.requireGroupeAccess(session.getGroupeId());
        session.setCloturee(true);
        return toSessionDto(sessionRepository.save(session));
    }

    /**
     * Obtenir des statistiques de présence pour un groupe.
     * Accessible par PRESIDENT (ses groupes) et LEADER (tous groupes).
     */
    public AttendanceStatsDto getGroupAttendanceStats(Long groupeId) {
        securityHelper.requireGroupeAccess(groupeId);

        List<AttendanceSession> sessions = sessionRepository.findByGroupeId(groupeId);
        long totalSessions = sessions.size();
        long totalPresents = 0;
        long totalAbsents = 0;
        long totalNonRenseignes = 0;

        for (AttendanceSession session : sessions) {
            totalPresents += recordRepository.countBySessionIdAndStatut(session.getId(), StatutPresence.PRESENT);
            totalAbsents += recordRepository.countBySessionIdAndStatut(session.getId(), StatutPresence.ABSENT);
            totalNonRenseignes += recordRepository.countBySessionIdAndStatut(session.getId(), StatutPresence.NON_RENSEIGNE);
        }

        long totalEnregistrements = totalPresents + totalAbsents;
        double tauxPresence = totalEnregistrements > 0 ? ((double) totalPresents / totalEnregistrements) * 100.0 : 0.0;

        return AttendanceStatsDto.builder()
                .groupeId(groupeId)
                .totalSessions(totalSessions)
                .totalPresents(totalPresents)
                .totalAbsents(totalAbsents)
                .totalNonRenseignes(totalNonRenseignes)
                .tauxPresencePercentage(Math.round(tauxPresence * 100.0) / 100.0)
                .build();
    }

    private AttendanceSessionDto toSessionDto(AttendanceSession session) {
        long totalMembres = recordRepository.countBySessionId(session.getId());
        long totalPresents = recordRepository.countBySessionIdAndStatut(session.getId(), StatutPresence.PRESENT);
        long totalAbsents = recordRepository.countBySessionIdAndStatut(session.getId(), StatutPresence.ABSENT);
        long totalNonRenseignes = recordRepository.countBySessionIdAndStatut(session.getId(), StatutPresence.NON_RENSEIGNE);

        return AttendanceSessionDto.builder()
                .id(session.getId())
                .titre(session.getTitre())
                .typeCulte(session.getTypeCulte())
                .dateSession(session.getDateSession())
                .groupeId(session.getGroupeId())
                .cloturee(session.isCloturee())
                .remarque(session.getRemarque())
                .libelle(session.getLibelle())
                .totalMembres(totalMembres)
                .totalPresents(totalPresents)
                .totalAbsents(totalAbsents)
                .totalNonRenseignes(totalNonRenseignes)
                .build();
    }
}
