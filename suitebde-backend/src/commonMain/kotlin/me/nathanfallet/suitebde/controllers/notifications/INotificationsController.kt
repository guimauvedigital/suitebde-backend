package me.nathanfallet.suitebde.controllers.notifications

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.notifications.NotificationTopics

interface INotificationsController :
    IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload> {

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
        @PathParameter associationId: String?,
        @Payload payload: CreateNotificationPayload,
    )

    @APIMapping("listNotificationTopic")
    @Path("GET", "/notifications/topics")
    @DocumentedError(403, "notifications_send_not_allowed")
    suspend fun topics(call: ApplicationCall, @PathParameter associationId: String?): NotificationTopics

}
