package me.nathanfallet.suitebde.controllers

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.plugins.Serialization
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class AbstractModelControllerTest {

    @Test
    fun testAPIv1GetRoute() = testApplication {
        environment {
            config = ApplicationConfig("application.test.conf")
        }
        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            json(Serialization.json)
        }
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Serialization.json)
            }
        }
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val association = Association("id", "name")
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1GetRoute(this)
        }
        assertEquals(listOf("mock"), client.get("").body())
    }

}