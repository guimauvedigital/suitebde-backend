package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateClubNotificationPayload
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ISendNotificationToClubUseCase : ISuspendUseCase<CreateClubNotificationPayload, Unit>
