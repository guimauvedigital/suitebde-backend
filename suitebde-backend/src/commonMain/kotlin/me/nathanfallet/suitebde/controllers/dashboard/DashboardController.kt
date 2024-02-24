package me.nathanfallet.suitebde.controllers.dashboard

import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.suitebde.usecases.associations.ICreateStripeAccountLinkUseCase
import me.nathanfallet.suitebde.usecases.associations.IRefreshStripeAccountUseCase
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase

class DashboardController(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val refreshStripeAccountUseCase: IRefreshStripeAccountUseCase,
    private val createStripeAccountLinkUseCase: ICreateStripeAccountLinkUseCase,
) : IDashboardController {

    override suspend fun dashboard() {}

    override suspend fun settings(call: ApplicationCall) {
        val association = requireAssociationForCallUseCase(call)
        refreshStripeAccountUseCase(association)
    }

    override suspend fun settingsStripe(call: ApplicationCall): RedirectResponse {
        val association = requireAssociationForCallUseCase(call)
        val returnUrl = "https://${call.request.host()}/admin/settings"
        return RedirectResponse(createStripeAccountLinkUseCase(association, returnUrl) ?: returnUrl)
    }

}
