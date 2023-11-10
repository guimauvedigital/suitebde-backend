package me.nathanfallet.suitebde.usecases.application

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.cloudflare.models.accounts.Account
import me.nathanfallet.cloudflare.models.zones.Zone
import me.nathanfallet.cloudflare.models.zones.ZonePayload
import kotlin.test.Test
import kotlin.test.assertEquals

class GetZoneForDomainUseCaseTest {

    private val zone = Zone("zoneId", "suitebde.com")

    @Test
    fun testInvokeExists() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val useCase = GetZoneForDomainUseCase(client, "accountId")
        coEvery { client.zones.list() } returns listOf(zone)
        assertEquals(zone, useCase("test.suitebde.com"))
    }

    @Test
    fun testInvokeCreate() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val useCase = GetZoneForDomainUseCase(client, "accountId")
        coEvery { client.zones.list() } returns listOf()
        coEvery {
            client.zones.create(ZonePayload("suitebde.com", Account("accountId")))
        } returns zone
        assertEquals(zone, useCase("test.suitebde.com"))
    }

}
