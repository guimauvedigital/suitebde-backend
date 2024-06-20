package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.CreateUserNotificationPayload
import dev.kaccelero.usecases.ISuspendUseCase

interface ISendNotificationToUserUseCase : ISuspendUseCase<CreateUserNotificationPayload, Unit>
