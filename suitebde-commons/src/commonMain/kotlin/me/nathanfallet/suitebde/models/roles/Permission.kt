package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable

@Serializable
enum class Permission {

    ADMIN,
    FILES_VIEW,
    NOTIFICATIONS_SEND,
    DOMAINS_CREATE,
    DOMAINS_UPDATE,
    DOMAINS_DELETE,
    SUBSCRIPTIONS_VIEW,
    SUBSCRIPTIONS_CREATE,
    SUBSCRIPTIONS_UPDATE,
    SUBSCRIPTIONS_DELETE,
    USERS_VIEW,
    USERS_UPDATE,
    ROLES_VIEW,
    ROLES_CREATE,
    ROLES_UPDATE,
    ROLES_DELETE,
    ROLES_USERS,
    ROLES_PERMISSIONS,
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
    CLUBS_VIEW,
    CLUBS_CREATE,
    CLUBS_UPDATE,
    CLUBS_DELETE,
    CLUBS_USERS,
    ;

    infix fun inAssociation(associationId: String) =
        PermissionInAssociation(this, associationId)

}
