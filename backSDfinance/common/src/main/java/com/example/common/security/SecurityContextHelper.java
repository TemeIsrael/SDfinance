package com.example.common.security;

import com.example.common.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class SecurityContextHelper {

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return auth.getName();
    }

    public UserRole getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return auth.getAuthorities().stream()
                .map(a -> {
                    try {
                        return UserRole.valueOf(a.getAuthority());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(r -> r != null)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<Long> getCurrentGroupIds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Collections.emptyList();
        }
        
        if (auth.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();
            if (details.containsKey("groupeIds")) {
                return (List<Long>) details.get("groupeIds");
            }
        }
        
        return Collections.emptyList();
    }

    public boolean isAdmin() {
        UserRole role = getCurrentRole();
        return role != null && role.isTechnicalOnly();
    }

    public boolean isLeader() {
        UserRole role = getCurrentRole();
        return role != null && role.isGlobalScope();
    }

    public boolean isGroupScoped() {
        UserRole role = getCurrentRole();
        return role != null && role.isGroupScoped();
    }

    public boolean hasAccessToGroup(Long targetGroupeId) {
        if (isAdmin()) return false;
        if (isLeader()) return true; 
        return getCurrentGroupIds().contains(targetGroupeId);
    }

    public void requireGroupeAccess(Long targetGroupeId) {
        if (!hasAccessToGroup(targetGroupeId)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied to group " + targetGroupeId);
        }
    }

    public void requireNotAdmin() {
        if (isAdmin()) {
            throw new org.springframework.security.access.AccessDeniedException("Admin cannot access business data");
        }
    }
    
    public void requireRole(UserRole requiredRole) {
        if (getCurrentRole() != requiredRole) {
            throw new org.springframework.security.access.AccessDeniedException("Requires role: " + requiredRole);
        }
    }
}
