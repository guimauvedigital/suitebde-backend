package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.CreateClubNotificationPayload
import dev.kaccelero.usecases.ISuspendUseCase

interface ISendNotificationToClubUseCase : ISuspendUseCase<CreateClubNotificationPayload, Unit>
