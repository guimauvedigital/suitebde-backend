package com.suitebde.client

import com.suitebde.repositories.associations.IAssociationsRemoteRepository
import com.suitebde.repositories.associations.IDomainsInAssociationsRemoteRepository
import com.suitebde.repositories.associations.ISubscriptionsInAssociationsRemoteRepository
import com.suitebde.repositories.auth.IAuthAPIRemoteRepository
import com.suitebde.repositories.clubs.IClubsRemoteRepository
import com.suitebde.repositories.clubs.IUsersInClubsRemoteRepository
import com.suitebde.repositories.events.IEventsRemoteRepository
import com.suitebde.repositories.notifications.INotificationTokensRemoteRepository
import com.suitebde.repositories.notifications.INotificationsRemoteRepository
import com.suitebde.repositories.roles.IPermissionsInRolesRemoteRepository
import com.suitebde.repositories.roles.IRolesRemoteRepository
import com.suitebde.repositories.roles.IUsersInRolesRemoteRepository
import com.suitebde.repositories.scans.IScansRemoteRepository
import com.suitebde.repositories.users.ISubscriptionsInUsersRemoteRepository
import com.suitebde.repositories.users.IUsersRemoteRepository
import com.suitebde.repositories.web.IWebMenusRemoteRepository
import com.suitebde.repositories.web.IWebPagesRemoteRepository
import dev.kaccelero.client.IAPIClient

interface ISuiteBDEClient : IAPIClient {

    val auth: IAuthAPIRemoteRepository
    val associations: IAssociationsRemoteRepository
    val domainsInAssociations: IDomainsInAssociationsRemoteRepository
    val subscriptionsInAssociations: ISubscriptionsInAssociationsRemoteRepository
    val notifications: INotificationsRemoteRepository
    val users: IUsersRemoteRepository
    val subscriptionsInUsers: ISubscriptionsInUsersRemoteRepository
    val notificationTokens: INotificationTokensRemoteRepository
    val scans: IScansRemoteRepository
    val webMenus: IWebMenusRemoteRepository
    val webPages: IWebPagesRemoteRepository
    val events: IEventsRemoteRepository
    val roles: IRolesRemoteRepository
    val usersInRoles: IUsersInRolesRemoteRepository
    val permissionsInRoles: IPermissionsInRolesRemoteRepository
    val clubs: IClubsRemoteRepository
    val usersInClubs: IUsersInClubsRemoteRepository

}
