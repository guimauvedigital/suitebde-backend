package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.suitebde.models.associations.Association

enum class Permission {

    ADMIN,
    DOMAINS_CREATE,
    DOMAINS_UPDATE,
    DOMAINS_DELETE,
    USERS_VIEW,
    USERS_UPDATE;

    infix fun inAssociation(association: Association) = PermissionInAssociation(this, association)

}
