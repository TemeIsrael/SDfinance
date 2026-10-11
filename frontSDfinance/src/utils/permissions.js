export const ROLES = {
  ADMIN: 'ADMIN',
  LEADER: 'LEADER',
  PRESIDENT: 'PRESIDENT',
  TRESORIER_CAISSIER: 'TRESORIER_CAISSIER',
  MEMBRE: 'MEMBRE',
  COLLABORATOR: 'COLLABORATOR',
};

// Matrice des permissions UI
export const PERMISSIONS = {
  users: {
    canManage: [ROLES.ADMIN],
  },
  members: {
    canView: [ROLES.PRESIDENT, ROLES.LEADER, ROLES.COLLABORATOR],
    canManage: [ROLES.PRESIDENT, ROLES.COLLABORATOR], // collaborators can add enfants
  },
  finance: {
    canView: [ROLES.PRESIDENT, ROLES.TRESORIER_CAISSIER, ROLES.LEADER, ROLES.COLLABORATOR],
    canViewPersonal: [ROLES.MEMBRE],
    canManage: [ROLES.COLLABORATOR, ROLES.TRESORIER_CAISSIER, ROLES.PRESIDENT], // Trésorier, Président et Collaborateur peuvent saisir/gérer les finances
  },
  events: {
    canView: [ROLES.PRESIDENT, ROLES.TRESORIER_CAISSIER, ROLES.LEADER], // Tresorier pour la finance liée
    canManage: [ROLES.PRESIDENT],
  },
  messaging: {
    // Tous ont accès à leur propre messagerie pour communiquer avec les autres
    canUse: [ROLES.ADMIN, ROLES.LEADER, ROLES.PRESIDENT, ROLES.TRESORIER_CAISSIER, ROLES.MEMBRE, ROLES.COLLABORATOR],
  }
};

// Duplicate PERMISSIONS block removed – consolidated permissions are defined above.


/**
 * Vérifie si le rôle de l'utilisateur l'autorise à effectuer une action donnée
 * @param {string} userRole - Rôle de l'utilisateur
 * @param {string} module - Le module concerné (ex: 'members', 'finance')
 * @param {string} action - L'action (ex: 'canView', 'canManage', 'canViewPersonal')
 * @returns {boolean}
 */
export function hasPermission(userRole, module, action) {
  if (!userRole || !PERMISSIONS[module] || !PERMISSIONS[module][action]) {
    return false;
  }
  return PERMISSIONS[module][action].includes(userRole);
}

/**
 * Vérifie si le rôle nécessite la sélection d'un groupe spécifique pour fonctionner.
 */
export function requiresGroupContext(userRole) {
  return [ROLES.PRESIDENT, ROLES.TRESORIER_CAISSIER, ROLES.MEMBRE].includes(userRole);
}
