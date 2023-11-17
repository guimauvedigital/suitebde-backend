package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.suitebde.models.associations.Association

enum class Permission {

    ADMIN,
    DOMAINS_CREATE,
    DOMAINS_UPDATE,
    DOMAINS_DELETE,
    USERS_VIEW,
    USERS_UPDATE,
    WEBPAGES_VIEW,
    WEBPAGES_CREATE,
    WEBPAGES_UPDATE,
    WEBPAGES_DELETE,
    WEBMENUS_VIEW,
    WEBMENUS_CREATE,
    WEBMENUS_UPDATE,
    WEBMENUS_DELETE,
    ;

    infix fun inAssociation(association: Association) = PermissionInAssociation(this, association)

}
