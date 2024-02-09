package me.nathanfallet.suitebde.controllers.users

import io.ktor.server.application.*
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class SubscriptionsInUsersController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getSubscriptionsInUsersUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, String>,
) : ISubscriptionsInUsersController {

    override suspend fun list(call: ApplicationCall, parent: User): List<SubscriptionInUser> {
        return getSubscriptionsInUsersUseCase(parent.id)
    }
}
