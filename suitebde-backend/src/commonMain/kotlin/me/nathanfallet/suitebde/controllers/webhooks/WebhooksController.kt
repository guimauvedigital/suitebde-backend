package me.nathanfallet.suitebde.controllers.webhooks

import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import io.ktor.server.application.*
import io.ktor.server.request.*

class WebhooksController(
    private val stripeSecret: String,
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
                // Create order
                if (session.paymentStatus == "paid") {
                    // Payment succeeded
                }
            }

            "checkout.session.async_payment_succeeded" -> {
                val session = stripeObject as Session
                // Payment succeeded
            }

            "checkout.session.async_payment_failed" -> {
                val session = stripeObject as Session
                // Payment failed
            }
        }
    }

}
