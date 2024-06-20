package com.suitebde.repositories.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.users.User
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIChildModelRemoteRepository
import io.ktor.util.reflect.*

class NotificationTokensRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<User, UUID, *, *, *>,
) : APIChildModelRemoteRepository<NotificationToken, String, CreateNotificationTokenPayload, Unit, UUID>(
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
        userId: UUID,
        associationId: UUID,
    ): NotificationToken? = create(
        payload,
        RecursiveId<User, UUID, UUID>(userId, RecursiveId<Association, UUID, Unit>(associationId)),
        null
    )

}
