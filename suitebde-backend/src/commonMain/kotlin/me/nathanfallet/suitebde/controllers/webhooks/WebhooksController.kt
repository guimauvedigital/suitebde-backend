package me.nathanfallet.suitebde.controllers.webhooks

import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.suitebde.models.stripe.UpdateStripeOrderPayload
import me.nathanfallet.suitebde.usecases.stripe.ICreateStripeOrderForSessionUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase

class WebhooksController(
    private val stripeSecret: String,
    private val createStripeOrderForSessionUseCase: ICreateStripeOrderForSessionUseCase,
    private val updateStripeOrderUseCase: IUpdateChildModelSuspendUseCase<StripeOrder, String, UpdateStripeOrderPayload, String>,
) : IWebhooksController {

    override suspend fun stripe(call: ApplicationCall) {
        // Decode event (payload)
        val payload = call.receiveText()
        val signature = call.request.headers["Stripe-Signature"]
        val event = Webhook.constructEvent(payload, signature, stripeSecret)

        // Decode event data
        val stripeObject = event.dataObjectDeserializer.`object`.get()
        when (event.type) {
            "checkout.session.completed" -> {
                val session = stripeObject as Session
                createStripeOrderForSessionUseCase(session) ?: return
                if (session.paymentStatus == "paid") updateStripeOrderUseCase(
                    session.id,
                    UpdateStripeOrderPayload(Clock.System.now()),
                    session.metadata["associationId"]!!
                )
            }

            "checkout.session.async_payment_succeeded" -> {
                val session = stripeObject as Session
                updateStripeOrderUseCase(
                    session.id,
                    UpdateStripeOrderPayload(Clock.System.now()),
                    session.metadata["associationId"]!!
                )
            }

            "checkout.session.async_payment_failed" -> {
                val session = stripeObject as Session
                // Payment failed
            }
        }
    }

}
