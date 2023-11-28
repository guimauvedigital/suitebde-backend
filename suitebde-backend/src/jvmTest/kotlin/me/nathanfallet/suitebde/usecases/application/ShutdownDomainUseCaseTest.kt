package me.nathanfallet.suitebde.usecases.application

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.cloudflare.models.dns.records.DNSRecord
import me.nathanfallet.cloudflare.models.zones.Zone
import kotlin.test.Test
import kotlin.test.assertEquals

class ShutdownDomainUseCaseTest {

    private val zone = Zone("zoneId", "suitebde.com")
    private val customZone = Zone("zoneId", "custom.com")
    private val record = DNSRecord("recordId", zone.id, zone.name, "test")
    private val customRecord = DNSRecord("recordId", customZone.id, customZone.name, "@")

    @Test
    fun invoke() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = ShutdownDomainUseCase(client, getZoneForDomainUseCase)
        coEvery { getZoneForDomainUseCase("test.suitebde.com") } returns zone
        coEvery { client.dnsRecords.list(zone.id) } returns listOf(record)
        coEvery { client.dnsRecords.delete(record.id, zone.id) } returns true
        assertEquals(true, useCase("test.suitebde.com"))
    }

    @Test
    fun invokeNotExists() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = ShutdownDomainUseCase(client, getZoneForDomainUseCase)
        coEvery { getZoneForDomainUseCase("test.suitebde.com") } returns zone
        coEvery { client.dnsRecords.list(zone.id) } returns listOf()
        assertEquals(false, useCase("test.suitebde.com"))
    }

    @Test
    fun invokeFails() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = ShutdownDomainUseCase(client, getZoneForDomainUseCase)
        coEvery { getZoneForDomainUseCase("test.suitebde.com") } returns zone
        coEvery { client.dnsRecords.list(zone.id) } returns listOf(record)
        coEvery { client.dnsRecords.delete(record.id, zone.id) } returns false
        assertEquals(false, useCase("test.suitebde.com"))
    }

    @Test
    fun invokeCustom() = runBlocking {
        val client = mockk<ICloudflareClient>()
        val getZoneForDomainUseCase = mockk<IGetZoneForDomainUseCase>()
        val useCase = ShutdownDomainUseCase(client, getZoneForDomainUseCase)
        coEvery { getZoneForDomainUseCase("custom.com") } returns customZone
        coEvery { client.dnsRecords.list(customZone.id) } returns listOf(customRecord)
        coEvery { client.dnsRecords.delete(customRecord.id, customZone.id) } returns true
        assertEquals(true, useCase("custom.com"))
    }

}
