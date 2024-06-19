package com.suitebde.usecases.notifications

import com.suitebde.models.associations.Association
import com.suitebde.models.notifications.NotificationTopics

class ListNotificationTopicsUseCase : IListNotificationTopicsUseCase {

    override suspend fun invoke(input: Association): NotificationTopics {
        // TODO: More topics available
        return NotificationTopics(
            mapOf(
                "associations_${input.id}" to input.name,
                "associations_${input.id}_events" to "${input.name} (events)"
            )
        )
    }

}
