package me.nathanfallet.suitebde.controllers.webhooks

import me.nathanfallet.ktorx.routers.api.APIUnitRouter

class WebhooksRouter(
    controller: IWebhooksController,
) : APIUnitRouter(
    controller,
    IWebhooksController::class,
    route = "webhooks",
    prefix = "/api/v1"
)
