package com.suitebde.controllers.webhooks

import dev.kaccelero.routers.APIUnitRouter

class WebhooksRouter(
    controller: IWebhooksController,
) : APIUnitRouter(
    controller,
    IWebhooksController::class,
    route = "webhooks",
    prefix = "/api/v1"
)
