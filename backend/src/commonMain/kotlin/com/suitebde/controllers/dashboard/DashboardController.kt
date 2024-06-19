package com.suitebde.controllers.dashboard

import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import com.suitebde.usecases.stripe.ICreateStripeAccountLinkUseCase
import com.suitebde.usecases.stripe.IHasStripeAccountLinkedUseCase
import com.suitebde.usecases.stripe.IRefreshStripeAccountUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import io.ktor.server.application.*
import io.ktor.server.request.*

class DashboardController(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val hasStripeAccountLinkedUseCase: IHasStripeAccountLinkedUseCase,
    private val refreshStripeAccountUseCase: IRefreshStripeAccountUseCase,
    private val createStripeAccountLinkUseCase: ICreateStripeAccountLinkUseCase,
) : IDashboardController {

    override suspend fun dashboard(call: ApplicationCall): Map<String, Any> {
        val association = requireAssociationForCallUseCase(call)

        // TODO: Add more data to the dashboard (counters, summaries, ...)
        return mapOf(
            "stripe" to hasStripeAccountLinkedUseCase(association)
        )
    }

    override suspend fun settings(call: ApplicationCall): Map<String, Any> {
        val association = requireAssociationForCallUseCase(call)
        val stripeAccounts = refreshStripeAccountUseCase(association)
        return mapOf(
            "stripe" to stripeAccounts
        )
    }

    override suspend fun settingsStripe(call: ApplicationCall): RedirectResponse {
        val association = requireAssociationForCallUseCase(call)
        val returnUrl = "https://${call.request.host()}/admin/settings"
        return RedirectResponse(createStripeAccountLinkUseCase(association, returnUrl) ?: returnUrl)
    }

}
