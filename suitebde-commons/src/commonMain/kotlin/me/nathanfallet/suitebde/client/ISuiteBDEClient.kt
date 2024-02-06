package me.nathanfallet.suitebde.client

import me.nathanfallet.ktorx.models.api.IAPIClient
import me.nathanfallet.ktorx.repositories.auth.IAuthAPIRemoteRepository
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRemoteRepository
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRemoteRepository
import me.nathanfallet.suitebde.repositories.clubs.IClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.clubs.IUsersInClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.events.IEventsRemoteRepository
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInRolesRemoteRepository
import me.nathanfallet.suitebde.repositories.roles.IRolesRemoteRepository
import me.nathanfallet.suitebde.repositories.roles.IUsersInRolesRemoteRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRemoteRepository
import me.nathanfallet.suitebde.repositories.web.IWebMenusRemoteRepository
import me.nathanfallet.suitebde.repositories.web.IWebPagesRemoteRepository

interface ISuiteBDEClient : IAPIClient {

    val auth: IAuthAPIRemoteRepository
    val associations: IAssociationsRemoteRepository
    val domainsInAssociations: IDomainsInAssociationsRemoteRepository
    val users: IUsersRemoteRepository
    val webMenus: IWebMenusRemoteRepository
    val webPages: IWebPagesRemoteRepository
    val events: IEventsRemoteRepository
    val roles: IRolesRemoteRepository
    val usersInRoles: IUsersInRolesRemoteRepository
    val permissionsInRoles: IPermissionsInRolesRemoteRepository
    val clubs: IClubsRemoteRepository
    val usersInClubs: IUsersInClubsRemoteRepository

}
