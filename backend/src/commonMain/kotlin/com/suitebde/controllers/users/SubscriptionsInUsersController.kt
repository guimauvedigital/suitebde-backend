package com.suitebde.controllers.users

import com.suitebde.models.notifications.CreateUserNotificationPayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.notifications.ISendNotificationToUserUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IGetChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IUpdateChildModelSuspendUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*

class SubscriptionsInUsersController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val listSubscriptionsInUsersUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, UUID>,
    private val getSubscriptionInUserUseCase: IGetChildModelSuspendUseCase<SubscriptionInUser, UUID, UUID>,
    private val createSubscriptionInUserUseCase: ICreateChildModelSuspendUseCase<SubscriptionInUser, CreateSubscriptionInUserPayload, UUID>,
    private val updateSubscriptionInUserUseCase: IUpdateChildModelSuspendUseCase<SubscriptionInUser, UUID, UpdateSubscriptionInUserPayload, UUID>,
    private val sendNotificationToUserUseCase: ISendNotificationToUserUseCase,
) : ISubscriptionsInUsersController {

    override suspend fun list(call: ApplicationCall, parent: User): List<SubscriptionInUser> {
        return listSubscriptionsInUsersUseCase(parent.id)
    }

    override suspend fun create(
        call: ApplicationCall,
        parent: User,
        payload: CreateSubscriptionInUserPayload,
    ): SubscriptionInUser {
        val user = requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_SUBSCRIPTIONS inAssociation parent.id)
        } as? User ?: throw ControllerException(
            HttpStatusCode.Forbidden, "subscriptions_create_not_allowed"
        )
        val subscription = createSubscriptionInUserUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        sendNotificationToUserUseCase(
            CreateUserNotificationPayload(
                parent.id,
                "notification_subscription_created_title",
                "notification_subscription_created_description",
                bodyArgs = listOf(user.firstName, subscription.subscription?.name ?: "?")
            )
        )
        return subscription
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: User,
        id: UUID,
        payload: UpdateSubscriptionInUserPayload,
    ): SubscriptionInUser {
        val user = requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_SUBSCRIPTIONS inAssociation parent.id)
        } as? User ?: throw ControllerException(
            HttpStatusCode.Forbidden, "subscriptions_update_not_allowed"
        )
        val subscription = getSubscriptionInUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "subscriptions_not_found"
        )
        val updatedSubscription = updateSubscriptionInUserUseCase(subscription.id, payload, parent.id)
            ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
        sendNotificationToUserUseCase(
            CreateUserNotificationPayload(
                parent.id,
                "notification_subscription_updated_title",
                "notification_subscription_updated_description",
                bodyArgs = listOf(user.firstName, updatedSubscription.subscription?.name ?: "?")
            )
        )
        return updatedSubscription
    }

}
