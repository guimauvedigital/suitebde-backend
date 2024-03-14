package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class DeleteUserUseCase(
    private val usersRepository: IUsersRepository,
    private val listSubscriptionInUserUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, String>,
    private val deleteSubscriptionInUserUseCase: IDeleteChildModelSuspendUseCase<SubscriptionInUser, String, String>,
    private val listNotificationTokenUseCase: IListChildModelSuspendUseCase<NotificationToken, String>,
    private val deleteNotificationTokenUseCase: IDeleteChildModelSuspendUseCase<NotificationToken, String, String>,
    private val listUserInRoleUseCase: IListChildModelSuspendUseCase<UserInRole, String>,
    private val deleteUserInRoleUseCase: IDeleteChildModelSuspendUseCase<UserInRole, String, String>,
    private val listUserInClubUseCase: IListChildModelSuspendUseCase<UserInClub, String>,
    private val deleteUserInClubUseCase: IDeleteChildModelSuspendUseCase<UserInClub, String, String>,
) : IDeleteChildModelSuspendUseCase<User, String, String> {

    override suspend fun invoke(input1: String, input2: String): Boolean =
        usersRepository.delete(input1, input2).also { success ->
            if (!success) return@also
            listSubscriptionInUserUseCase(input1).forEach {
                deleteSubscriptionInUserUseCase(it.id, input1)
            }
            listNotificationTokenUseCase(input1).forEach {
                deleteNotificationTokenUseCase(it.id, input1)
            }
            listUserInRoleUseCase(input1).forEach {
                deleteUserInRoleUseCase(it.id, input1)
            }
            listUserInClubUseCase(input1).forEach {
                deleteUserInClubUseCase(it.id, input1)
            }
        }

}
