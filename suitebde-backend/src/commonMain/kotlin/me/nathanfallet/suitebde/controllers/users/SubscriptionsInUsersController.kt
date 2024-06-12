package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.notifications.CreateUserNotificationPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.notifications.ISendNotificationToUserUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class SubscriptionsInUsersController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val listSubscriptionsInUsersUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, String>,
    private val getSubscriptionInUserUseCase: IGetChildModelSuspendUseCase<SubscriptionInUser, String, String>,
    private val createSubscriptionInUserUseCase: ICreateChildModelSuspendUseCase<SubscriptionInUser, CreateSubscriptionInUserPayload, String>,
    private val updateSubscriptionInUserUseCase: IUpdateChildModelSuspendUseCase<SubscriptionInUser, String, UpdateSubscriptionInUserPayload, String>,
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
        id: String,
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
