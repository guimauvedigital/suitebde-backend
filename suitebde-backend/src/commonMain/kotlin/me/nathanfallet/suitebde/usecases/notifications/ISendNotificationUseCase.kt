package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface ISendNotificationUseCase : IPairSuspendUseCase<String, CreateNotificationPayload, Boolean>
