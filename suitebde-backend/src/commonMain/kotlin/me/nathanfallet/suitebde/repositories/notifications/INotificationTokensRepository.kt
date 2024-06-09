package me.nathanfallet.suitebde.repositories.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface INotificationTokensRepository :
    IChildModelSuspendRepository<NotificationToken, String, CreateNotificationTokenPayload, Unit, String> {

    suspend fun listByPermission(permission: Permission): List<NotificationToken>
    suspend fun listByClub(clubId: String): List<NotificationToken>
    suspend fun delete(token: String): Boolean

}
