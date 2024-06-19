package com.suitebde.usecases.notifications

import com.suitebde.models.associations.Association
import com.suitebde.models.notifications.NotificationTopics
import dev.kaccelero.usecases.ISuspendUseCase

interface IListNotificationTopicsUseCase : ISuspendUseCase<Association, NotificationTopics>
