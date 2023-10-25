package me.nathanfallet.suitebde.controllers

import io.ktor.server.routing.*

interface IController {

    fun createRoutes(root: Route)

}