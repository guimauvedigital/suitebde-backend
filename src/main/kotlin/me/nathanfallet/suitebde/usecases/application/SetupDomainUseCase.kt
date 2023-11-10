package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.cloudflare.models.dns.records.DNSRecordPayload

class SetupDomainUseCase(
    private val cloudflareClient: ICloudflareClient,
    private val getZoneForDomainUseCase: IGetZoneForDomainUseCase,
    private val environment: String
) : ISetupDomainUseCase {

    override suspend fun invoke(input: String): Boolean {
        val content = when (environment) {
            "production" -> "suitebde.com"
            "dev" -> "suitebde.dev"
            else -> return true // Do nothing as we are in local environment
        }
        val zone = getZoneForDomainUseCase(input) ?: return false
        val name = if (input == zone.name) "@" else input.removeSuffix("." + zone.name)
        cloudflareClient.dnsRecords.list(zone.id).firstOrNull { it.name == name }?.let { return false }
        cloudflareClient.dnsRecords.create(
            DNSRecordPayload(name, content, "CNAME", true),
            zone.id
        ) ?: return false
        return true
    }

}
