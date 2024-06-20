package com.suitebde.repositories.notifications

import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.roles.Permission
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface INotificationTokensRepository :
    IChildModelSuspendRepository<NotificationToken, String, CreateNotificationTokenPayload, Unit, UUID> {

    suspend fun listByPermission(permission: Permission): List<NotificationToken>
    suspend fun listByClub(clubId: UUID): List<NotificationToken>
    suspend fun delete(token: String): Boolean

}
