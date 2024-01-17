package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.suitebde.models.associations.Association

enum class Permission {

    ADMIN,
    DOMAINS_CREATE,
    DOMAINS_UPDATE,
    DOMAINS_DELETE,
    USERS_VIEW,
    USERS_UPDATE,
    ROLES_VIEW,
    ROLES_CREATE,
    ROLES_UPDATE,
    ROLES_DELETE,
    WEBPAGES_VIEW,
    WEBPAGES_CREATE,
    WEBPAGES_UPDATE,
    WEBPAGES_DELETE,
    WEBMENUS_VIEW,
    WEBMENUS_CREATE,
    WEBMENUS_UPDATE,
    WEBMENUS_DELETE,
    EVENTS_VIEW,
    EVENTS_CREATE,
    EVENTS_UPDATE,
    EVENTS_DELETE,
    ;

    infix fun inAssociation(association: Association) =
        PermissionInAssociation(this, association)

}
