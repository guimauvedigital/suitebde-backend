package com.suitebde.repositories.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.models.notifications.NotificationTopics
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.models.UnitModel
import dev.kaccelero.repositories.APIModelRemoteRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*

class NotificationsRemoteRepository(
    client: ISuiteBDEClient,
) : APIModelRemoteRepository<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    typeInfo<List<Association>>(),
    client,
    prefix = "/api/v1"
), INotificationsRemoteRepository {

    override suspend fun send(payload: CreateNotificationPayload, associationId: UUID) = client.request(
        HttpMethod.Post,
        "${constructFullRoute(RecursiveId<UnitModel, Unit, Unit>(Unit))}/$associationId/notifications"
    ) {
        contentType(ContentType.Application.Json)
        setBody(payload)
    }.let {}

    override suspend fun topics(associationId: UUID): NotificationTopics = client.request(
        HttpMethod.Get,
        "${constructFullRoute(RecursiveId<UnitModel, Unit, Unit>(Unit))}/$associationId/notifications/topics"
    ).body()

}
