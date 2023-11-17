package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.suitebde.models.associations.Association

enum class Permission {

    ADMIN,
    DOMAINS_CREATE,
    DOMAINS_UPDATE,
    DOMAINS_DELETE,
    USERS_VIEW,
    USERS_UPDATE,
    WEB_PAGES_CREATE,
    WEB_PAGES_UPDATE,
    WEB_PAGES_DELETE,
    WEB_MENUS_CREATE,
    WEB_MENUS_UPDATE,
    WEB_MENUS_DELETE,
    ;

    infix fun inAssociation(association: Association) = PermissionInAssociation(this, association)

}
