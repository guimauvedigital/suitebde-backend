package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateUserNotificationPayload
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ISendNotificationToUserUseCase : ISuspendUseCase<CreateUserNotificationPayload, Unit>
