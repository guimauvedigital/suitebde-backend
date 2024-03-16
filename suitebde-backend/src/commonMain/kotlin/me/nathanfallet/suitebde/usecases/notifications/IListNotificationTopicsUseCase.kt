package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.notifications.NotificationTopics
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IListNotificationTopicsUseCase : ISuspendUseCase<Association, NotificationTopics>
