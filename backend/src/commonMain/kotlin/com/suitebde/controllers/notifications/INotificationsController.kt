package com.suitebde.controllers.notifications

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.models.notifications.NotificationTopics
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface INotificationsController :
    IModelController<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload> {

    @AdminTemplateMapping("admin/notifications/send.ftl")
    @Path("GET", "/notifications")
    suspend fun form(call: ApplicationCall): Map<String, Any>

    @APIMapping("sendNotification")
    @AdminTemplateMapping("admin/notifications/send.ftl")
    @Path("POST", "/notifications")
    @DocumentedError(400, "notifications_topic_invalid")
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "notifications_send_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun send(
        call: ApplicationCall,
        @PathParameter associationId: UUID?,
        @Payload payload: CreateNotificationPayload,
    )

    @APIMapping("listNotificationTopic")
    @Path("GET", "/notifications/topics")
    @DocumentedError(403, "notifications_send_not_allowed")
    suspend fun topics(call: ApplicationCall, @PathParameter associationId: UUID?): NotificationTopics

}
