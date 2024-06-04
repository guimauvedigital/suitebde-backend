package me.nathanfallet.suitebde.repositories.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface INotificationTokensRepository :
    IChildModelSuspendRepository<NotificationToken, String, CreateNotificationTokenPayload, Unit, String> {

    suspend fun listByClub(clubId: String): List<NotificationToken>
    suspend fun delete(token: String): Boolean

}
