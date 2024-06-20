package com.suitebde.usecases.users

import com.suitebde.models.clubs.UserInClub
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.roles.UserInRole
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class DeleteUserUseCase(
    private val usersRepository: IUsersRepository,
    private val listSubscriptionInUserUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, UUID>,
    private val deleteSubscriptionInUserUseCase: IDeleteChildModelSuspendUseCase<SubscriptionInUser, UUID, UUID>,
    private val listNotificationTokenUseCase: IListChildModelSuspendUseCase<NotificationToken, UUID>,
    private val deleteNotificationTokenUseCase: IDeleteChildModelSuspendUseCase<NotificationToken, String, UUID>,
    private val listUserInRoleUseCase: IListChildModelSuspendUseCase<UserInRole, UUID>,
    private val deleteUserInRoleUseCase: IDeleteChildModelSuspendUseCase<UserInRole, UUID, UUID>,
    private val listUserInClubUseCase: IListChildModelSuspendUseCase<UserInClub, UUID>,
    private val deleteUserInClubUseCase: IDeleteChildModelSuspendUseCase<UserInClub, UUID, UUID>,
) : IDeleteChildModelSuspendUseCase<User, UUID, UUID> {

    override suspend fun invoke(input1: UUID, input2: UUID): Boolean =
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
