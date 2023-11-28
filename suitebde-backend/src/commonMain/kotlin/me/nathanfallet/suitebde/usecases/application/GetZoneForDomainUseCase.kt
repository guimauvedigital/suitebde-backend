package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.cloudflare.models.accounts.Account
import me.nathanfallet.cloudflare.models.zones.Zone
import me.nathanfallet.cloudflare.models.zones.ZonePayload

class GetZoneForDomainUseCase(
    private val cloudflareClient: ICloudflareClient,
    private val accountId: String
) : IGetZoneForDomainUseCase {

    override suspend fun invoke(input: String): Zone? {
        val domain = input.split(".").takeLast(2).joinToString(".")
        // TODO: Use search instead of list
        return cloudflareClient.zones.list().firstOrNull {
            it.name == domain
        } ?: cloudflareClient.zones.create(ZonePayload(domain, Account(accountId)))
    }

}
