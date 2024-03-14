package me.nathanfallet.suitebde.repositories.notifications

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.id.RecursiveId

class NotificationTokensRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<User, String, *, *, *>,
) : APIChildModelRemoteRepository<NotificationToken, String, CreateNotificationTokenPayload, Unit, String>(
    typeInfo<NotificationToken>(),
    typeInfo<CreateNotificationTokenPayload>(),
    typeInfo<Unit>(),
    typeInfo<List<NotificationToken>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), INotificationTokensRemoteRepository {

    override suspend fun create(
        payload: CreateNotificationTokenPayload,
        userId: String,
        associationId: String,
    ): NotificationToken? = create(
        payload,
        RecursiveId<User, String, String>(userId, RecursiveId<Association, String, Unit>(associationId)),
        null
    )

}
