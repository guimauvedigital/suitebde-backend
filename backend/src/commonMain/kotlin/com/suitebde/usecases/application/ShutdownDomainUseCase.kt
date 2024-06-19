package com.suitebde.usecases.application

import me.nathanfallet.cloudflare.client.ICloudflareClient

class ShutdownDomainUseCase(
    private val cloudflareClient: ICloudflareClient,
    private val getZoneForDomainUseCase: IGetZoneForDomainUseCase,
) : IShutdownDomainUseCase {

    override suspend fun invoke(input: String): Boolean {
        val zone = getZoneForDomainUseCase(input) ?: return false
        val name = if (input == zone.name) "@" else input.removeSuffix("." + zone.name)
        val record = cloudflareClient.dnsRecords.list(zone.id).firstOrNull { it.name == name } ?: return false
        return cloudflareClient.dnsRecords.delete(record.id, zone.id)
    }

}
