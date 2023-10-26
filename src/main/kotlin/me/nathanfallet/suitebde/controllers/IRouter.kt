package me.nathanfallet.suitebde.controllers

import io.ktor.server.routing.*

interface IRouter {

    fun createRoutes(root: Route)

}