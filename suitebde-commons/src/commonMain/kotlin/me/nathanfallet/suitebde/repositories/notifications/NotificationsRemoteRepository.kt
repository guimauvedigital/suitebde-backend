package me.nathanfallet.suitebde.repositories.notifications

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.notifications.NotificationTopics
import me.nathanfallet.usecases.models.UnitModel
import me.nathanfallet.usecases.models.id.RecursiveId

class NotificationsRemoteRepository(
    client: ISuiteBDEClient,
) : APIModelRemoteRepository<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    typeInfo<List<Association>>(),
    client,
    prefix = "/api/v1"
), INotificationsRemoteRepository {

    override suspend fun send(payload: CreateNotificationPayload, associationId: String) = client.request(
        HttpMethod.Post,
        "${constructFullRoute(RecursiveId<UnitModel, Unit, Unit>(Unit))}/$associationId/notifications"
    ) {
        contentType(ContentType.Application.Json)
        setBody(payload)
    }.let {}

    override suspend fun topics(associationId: String): NotificationTopics = client.request(
        HttpMethod.Get,
        "${constructFullRoute(RecursiveId<UnitModel, Unit, Unit>(Unit))}/$associationId/notifications/topics"
    ).body()

}
