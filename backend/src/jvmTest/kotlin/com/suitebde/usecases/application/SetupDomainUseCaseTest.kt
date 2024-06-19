package com.suitebde.usecases.application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.cloudflare.models.dns.records.DNSRecord
import me.nathanfallet.cloudflare.models.dns.records.DNSRecordPayload
import me.nathanfallet.cloudflare.models.zones.Zone
import kotlin.test.Test
import kotlin.test.assertEquals

class SetupDomainUseCaseTest {

    private val zone = Zone("zoneId", "suitebde.com")
    private val devZone = Zone("zoneId", "suitebde.dev")
    private val customZone = Zone("zoneId", "custom.com")

    @Test
    fun invokeLocalhost() = runBlocking {
        val useCase = SetupDomainUseCase(mockk(), mockk(), "localhost")
        assertEquals(true, useCase("test.suitebde.com"))
    }

    @Test
    fun invokeProduction() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = SetupDomainUseCase(client, getZoneForDomainUseCase, "production")
        val payload = DNSRecordPayload("test", "suitebde.com", "CNAME", true)
        coEvery { getZoneForDomainUseCase("test.suitebde.com") } returns zone
        coEvery { client.dnsRecords.list(zone.id) } returns listOf()
        coEvery { client.dnsRecords.create(payload, zone.id) } returns DNSRecord("recordId", zone.id)
        assertEquals(true, useCase("test.suitebde.com"))
        coVerify { client.dnsRecords.create(payload, zone.id) }
    }

    @Test
    fun invokeDev() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = SetupDomainUseCase(client, getZoneForDomainUseCase, "dev")
        val payload = DNSRecordPayload("test", "suitebde.dev", "CNAME", true)
        coEvery { getZoneForDomainUseCase("test.suitebde.dev") } returns devZone
        coEvery { client.dnsRecords.list(devZone.id) } returns listOf()
        coEvery { client.dnsRecords.create(payload, devZone.id) } returns DNSRecord("recordId", devZone.id)
        assertEquals(true, useCase("test.suitebde.dev"))
        coVerify { client.dnsRecords.create(payload, devZone.id) }
    }

    @Test
    fun invokeCustom() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = SetupDomainUseCase(client, getZoneForDomainUseCase, "production")
        val payload = DNSRecordPayload("@", "suitebde.com", "CNAME", true)
        coEvery { getZoneForDomainUseCase("custom.com") } returns customZone
        coEvery { client.dnsRecords.list(customZone.id) } returns listOf()
        coEvery { client.dnsRecords.create(payload, customZone.id) } returns DNSRecord("recordId", customZone.id)
        assertEquals(true, useCase("custom.com"))
        coVerify { client.dnsRecords.create(payload, customZone.id) }
    }

    @Test
    fun invokeNotAvailable() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = SetupDomainUseCase(client, getZoneForDomainUseCase, "production")
        coEvery { getZoneForDomainUseCase("test.suitebde.com") } returns zone
        coEvery { client.dnsRecords.list(zone.id) } returns listOf(
            DNSRecord("recordId", zone.id, zone.name, "test")
        )
        assertEquals(false, useCase("test.suitebde.com"))
    }

    @Test
    fun invokeCreateFails() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = SetupDomainUseCase(client, getZoneForDomainUseCase, "production")
        val payload = DNSRecordPayload("test", "suitebde.com", "CNAME", true)
        coEvery { getZoneForDomainUseCase("test.suitebde.com") } returns zone
        coEvery { client.dnsRecords.list(zone.id) } returns listOf()
        coEvery { client.dnsRecords.create(payload, zone.id) } returns null
        assertEquals(false, useCase("test.suitebde.com"))
    }

}
