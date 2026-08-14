package com.example.common.model;

public enum UserRole {
    ADMIN,
    LEADER,
    PRESIDENT,
    TRESORIER_CAISSIER,
    MEMBRE;

    public boolean isGlobalScope() {
        return this == LEADER;
    }

    public boolean canWriteFinance() {
        return this == TRESORIER_CAISSIER;
    }

    public boolean canReadAllGroupsSummary() {
        return this == LEADER;
    }

    public boolean canManageEvents() {
        return this == PRESIDENT;
    }

    public boolean isTechnicalOnly() {
        return this == ADMIN;
    }

    public boolean isGroupScoped() {
        return this == PRESIDENT || this == TRESORIER_CAISSIER || this == MEMBRE;
    }
}
