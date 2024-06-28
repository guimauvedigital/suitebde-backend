package com.suitebde.plugins

import com.suitebde.controllers.associations.*
import com.suitebde.controllers.auth.AuthController
import com.suitebde.controllers.auth.AuthRouter
import com.suitebde.controllers.auth.IAuthController
import com.suitebde.controllers.clubs.*
import com.suitebde.controllers.dashboard.DashboardController
import com.suitebde.controllers.dashboard.DashboardRouter
import com.suitebde.controllers.dashboard.IDashboardController
import com.suitebde.controllers.events.EventsController
import com.suitebde.controllers.events.EventsRouter
import com.suitebde.controllers.events.IEventsController
import com.suitebde.controllers.files.FilesController
import com.suitebde.controllers.files.FilesRouter
import com.suitebde.controllers.files.IFilesController
import com.suitebde.controllers.notifications.*
import com.suitebde.controllers.roles.*
import com.suitebde.controllers.root.IRootController
import com.suitebde.controllers.root.RootController
import com.suitebde.controllers.root.RootRouter
import com.suitebde.controllers.scans.IScansController
import com.suitebde.controllers.scans.ScansController
import com.suitebde.controllers.scans.ScansRouter
import com.suitebde.controllers.users.*
import com.suitebde.controllers.web.*
import com.suitebde.controllers.webhooks.IWebhooksController
import com.suitebde.controllers.webhooks.WebhooksController
import com.suitebde.controllers.webhooks.WebhooksRouter
import com.suitebde.database.Database
import com.suitebde.database.application.ClientsDatabaseRepository
import com.suitebde.database.associations.AssociationsDatabaseRepository
import com.suitebde.database.associations.CodesInEmailsDatabaseRepository
import com.suitebde.database.associations.DomainsInAssociationsDatabaseRepository
import com.suitebde.database.associations.SubscriptionsInAssociationsDatabaseRepository
import com.suitebde.database.clubs.ClubsDatabaseRepository
import com.suitebde.database.clubs.RolesInClubsDatabaseRepository
import com.suitebde.database.clubs.UsersInClubsDatabaseRepository
import com.suitebde.database.events.EventsDatabaseRepository
import com.suitebde.database.notifications.NotificationTokensDatabaseRepository
import com.suitebde.database.roles.PermissionsInRolesDatabaseRepository
import com.suitebde.database.roles.RolesDatabaseRepository
import com.suitebde.database.roles.UsersInRolesDatabaseRepository
import com.suitebde.database.scans.ScansDatabaseRepository
import com.suitebde.database.stripe.StripeAccountsDatabaseRepository
import com.suitebde.database.stripe.StripeOrdersDatabaseRepository
import com.suitebde.database.users.ClientsInUsersDatabaseRepository
import com.suitebde.database.users.SubscriptionsInUsersDatabaseRepository
import com.suitebde.database.users.UsersDatabaseRepository
import com.suitebde.database.web.WebMenusDatabaseRepository
import com.suitebde.database.web.WebPagesDatabaseRepository
import com.suitebde.models.application.Client
import com.suitebde.models.associations.*
import com.suitebde.models.clubs.*
import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.roles.*
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.models.stripe.*
import com.suitebde.models.users.*
import com.suitebde.models.web.*
import com.suitebde.repositories.associations.IAssociationsRepository
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import com.suitebde.repositories.associations.IDomainsInAssociationsRepository
import com.suitebde.repositories.associations.ISubscriptionsInAssociationsRepository
import com.suitebde.repositories.clubs.IClubsRepository
import com.suitebde.repositories.clubs.IRolesInClubsRepository
import com.suitebde.repositories.clubs.IUsersInClubsRepository
import com.suitebde.repositories.events.IEventsRepository
import com.suitebde.repositories.notifications.INotificationTokensRepository
import com.suitebde.repositories.roles.IPermissionsInRolesRepository
import com.suitebde.repositories.roles.IRolesRepository
import com.suitebde.repositories.roles.IUsersInRolesRepository
import com.suitebde.repositories.scans.IScansRepository
import com.suitebde.repositories.stripe.IStripeAccountsRepository
import com.suitebde.repositories.stripe.IStripeOrdersRepository
import com.suitebde.repositories.users.IClientsInUsersRepository
import com.suitebde.repositories.users.ISubscriptionsInUsersRepository
import com.suitebde.repositories.users.IUsersRepository
import com.suitebde.repositories.web.IWebMenusRepository
import com.suitebde.repositories.web.IWebPagesRepository
import com.suitebde.services.emails.EmailsService
import com.suitebde.services.emails.IEmailsService
import com.suitebde.services.firebase.FirebaseService
import com.suitebde.services.firebase.IFirebaseService
import com.suitebde.services.jwt.IJWTService
import com.suitebde.services.jwt.JWTService
import com.suitebde.services.stripe.IStripeService
import com.suitebde.services.stripe.StripeService
import com.suitebde.usecases.application.*
import com.suitebde.usecases.associations.*
import com.suitebde.usecases.auth.*
import com.suitebde.usecases.clubs.CreateClubUseCase
import com.suitebde.usecases.clubs.DeleteClubUseCase
import com.suitebde.usecases.events.IListAllEventsUseCase
import com.suitebde.usecases.events.ListAllEventsUseCase
import com.suitebde.usecases.files.IListFilesUseCase
import com.suitebde.usecases.files.IUploadFileUseCase
import com.suitebde.usecases.files.ListFilesUseCase
import com.suitebde.usecases.files.UploadFileUseCase
import com.suitebde.usecases.notifications.*
import com.suitebde.usecases.roles.CheckPermissionUseCase
import com.suitebde.usecases.roles.GetPermissionsForUserUseCase
import com.suitebde.usecases.roles.IGetPermissionsForUserUseCase
import com.suitebde.usecases.scans.IListScansForDaysUseCase
import com.suitebde.usecases.scans.ListScansForDaysUseCase
import com.suitebde.usecases.stripe.*
import com.suitebde.usecases.users.*
import com.suitebde.usecases.web.*
import dev.kaccelero.commons.emails.ISendEmailUseCase
import dev.kaccelero.commons.localization.GetLocaleForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.localization.TranslateFromPropertiesUseCase
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.sessions.ISessionsRepository
import dev.kaccelero.commons.sessions.SessionsDatabaseRepository
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.commons.users.RequireUserForCallUseCase
import dev.kaccelero.database.IDatabase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IModelSuspendRepository
import io.ktor.server.application.*
import me.nathanfallet.cloudflare.client.CloudflareClient
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.cloudflare.r2.IR2Client
import me.nathanfallet.cloudflare.r2.R2Client
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        val databaseModule = module {
            single<IDatabase> {
                Database(
                    environment.config.property("database.protocol").getString(),
                    environment.config.property("database.host").getString(),
                    environment.config.property("database.name").getString(),
                    environment.config.property("database.user").getString(),
                    environment.config.property("database.password").getString()
                )
            }
        }
        val serviceModule = module {
            single<IEmailsService> {
                EmailsService(
                    environment.config.property("email.host").getString(),
                    environment.config.property("email.username").getString(),
                    environment.config.property("email.password").getString()
                )
            }
            single<IFirebaseService> { FirebaseService() }
            single<IJWTService> {
                JWTService(
                    environment.config.property("jwt.secret").getString(),
                    environment.config.property("jwt.issuer").getString()
                )
            }
            single<IStripeService> {
                StripeService(
                    environment.config.property("stripe.key").getString()
                )
            }
            single<ICloudflareClient> {
                CloudflareClient(
                    environment.config.property("cloudflare.token").getString()
                )
            }
            single<IR2Client> {
                R2Client(
                    environment.config.property("cloudflare.id").getString(),
                    environment.config.property("cloudflare.secret").getString(),
                    environment.config.property("cloudflare.account").getString()
                )
            }
        }
        val repositoryModule = module {
            // Application
            single<IModelSuspendRepository<Client, UUID, Unit, Unit>>(named<Client>()) {
                ClientsDatabaseRepository(get())
            }
            single<ISessionsRepository> { SessionsDatabaseRepository(get()) }

            // Associations
            single<IAssociationsRepository> { AssociationsDatabaseRepository(get()) }
            single<ICodesInEmailsRepository> { CodesInEmailsDatabaseRepository(get()) }
            single<IDomainsInAssociationsRepository> { DomainsInAssociationsDatabaseRepository(get()) }
            single<ISubscriptionsInAssociationsRepository> { SubscriptionsInAssociationsDatabaseRepository(get()) }

            // Stripe
            single<IStripeAccountsRepository> { StripeAccountsDatabaseRepository(get()) }
            single<IStripeOrdersRepository> { StripeOrdersDatabaseRepository(get()) }

            // Users
            single<IUsersRepository> { UsersDatabaseRepository(get()) }
            single<IClientsInUsersRepository> { ClientsInUsersDatabaseRepository(get()) }
            single<ISubscriptionsInUsersRepository> { SubscriptionsInUsersDatabaseRepository(get()) }

            // Scans
            single<IScansRepository> { ScansDatabaseRepository(get()) }

            // Notifications
            single<INotificationTokensRepository> { NotificationTokensDatabaseRepository(get()) }

            // Roles
            single<IRolesRepository> { RolesDatabaseRepository(get()) }
            single<IPermissionsInRolesRepository> { PermissionsInRolesDatabaseRepository(get()) }
            single<IUsersInRolesRepository> { UsersInRolesDatabaseRepository(get()) }

            // Web
            single<IWebPagesRepository> { WebPagesDatabaseRepository(get()) }
            single<IWebMenusRepository> { WebMenusDatabaseRepository(get()) }

            // Events
            single<IEventsRepository> { EventsDatabaseRepository(get()) }

            // Clubs
            single<IClubsRepository> { ClubsDatabaseRepository(get()) }
            single<IRolesInClubsRepository> { RolesInClubsDatabaseRepository(get()) }
            single<IUsersInClubsRepository> { UsersInClubsDatabaseRepository(get(), get()) }
        }
        val useCaseModule = module {
            // Application
            single<ISendEmailUseCase> { SendEmailUseCase(get()) }
            single<IExpireUseCase> {
                ExpireUseCase(
                    get(), get(),
                    get(), get(),
                    get(), get(named<User>()),
                )
            }
            single<ITranslateUseCase> { TranslateFromPropertiesUseCase() }
            single<IGetLocaleForCallUseCase> { GetLocaleForCallUseCase() }
            single<IAddDurationUseCase> { AddDurationUseCase() }
            single<IGetZoneForDomainUseCase> {
                GetZoneForDomainUseCase(
                    get(),
                    environment.config.property("cloudflare.account").getString()
                )
            }
            single<ISetupDomainUseCase> {
                SetupDomainUseCase(
                    get(),
                    get(),
                    environment.config.property("ktor.environment").getString()
                )
            }
            single<IShutdownDomainUseCase> { ShutdownDomainUseCase(get(), get()) }
            single<IGetModelSuspendUseCase<Client, UUID>>(named<Client>()) {
                GetModelFromRepositorySuspendUseCase(get(named<Client>()))
            }

            // Associations
            single<IGetAssociationsUseCase> { GetAssociationsUseCase(get()) }
            single<IGetAssociationForCallUseCase> { GetAssociationForCallUseCase(get()) }
            single<IRequireAssociationForCallUseCase> { RequireAssociationForCallUseCase(get()) }
            single<IGetModelSuspendUseCase<Association, UUID>>(named<Association>()) {
                GetModelFromRepositorySuspendUseCase(get<IAssociationsRepository>())
            }
            single<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>(named<Association>()) {
                CreateAssociationUseCase(
                    get(),
                    get(named<User>()),
                    get(named<WebPage>()),
                    get(named<WebMenu>())
                )
            }
            single<IUpdateModelSuspendUseCase<Association, UUID, UpdateAssociationPayload>>(named<Association>()) {
                UpdateModelFromRepositorySuspendUseCase(get<IAssociationsRepository>())
            }
            single<IDeleteModelSuspendUseCase<Association, UUID>>(named<Association>()) {
                DeleteAssociationUseCase(
                    get(),
                    get(named<User>()),
                    get(named<User>()),
                    get(named<Club>()),
                    get(named<Club>())
                )
            }

            // Codes in emails
            single<IGetCodeInEmailUseCase> { GetCodeInEmailUseCase(get()) }
            single<ICreateCodeInEmailUseCase> { CreateCodeInEmailUseCase(get(), get()) }
            single<IDeleteCodeInEmailUseCase> { DeleteCodeInEmailUseCase(get()) }

            // Domains in associations
            single<IListChildModelSuspendUseCase<DomainInAssociation, UUID>>(named<DomainInAssociation>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<DomainInAssociation, UUID>>(named<DomainInAssociation>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
            }
            single<IGetChildModelSuspendUseCase<DomainInAssociation, String, UUID>>(named<DomainInAssociation>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, UUID>>(named<DomainInAssociation>()) {
                CreateDomainInAssociationUseCase(get(), get())
            }
            single<IDeleteChildModelSuspendUseCase<DomainInAssociation, String, UUID>>(named<DomainInAssociation>()) {
                DeleteDomainInAssociationUseCase(get(), get())
            }

            // Subscriptions in associations
            single<IListChildModelSuspendUseCase<SubscriptionInAssociation, UUID>>(named<SubscriptionInAssociation>()) {
                ListChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<SubscriptionInAssociation, UUID>>(named<SubscriptionInAssociation>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IGetChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UUID>>(named<SubscriptionInAssociation>()) {
                GetChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<SubscriptionInAssociation, CreateSubscriptionInAssociationPayload, UUID>>(
                named<SubscriptionInAssociation>()
            ) {
                CreateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UUID>>(named<SubscriptionInAssociation>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UpdateSubscriptionInAssociationPayload, UUID>>(
                named<SubscriptionInAssociation>()
            ) {
                UpdateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }

            // Stripe
            single<IHasStripeAccountLinkedUseCase> { HasStripeAccountLinkedUseCase(get()) }
            single<IListChildModelSuspendUseCase<StripeAccount, UUID>>(named<StripeAccount>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<IGetChildModelSuspendUseCase<StripeAccount, String, UUID>>(named<StripeAccount>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<StripeAccount, CreateStripeAccountPayload, UUID>>(
                named<StripeAccount>()
            ) {
                CreateChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<StripeAccount, String, UpdateStripeAccountPayload, UUID>>(
                named<StripeAccount>()
            ) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<IRefreshStripeAccountUseCase> {
                RefreshStripeAccountUseCase(get(), get(named<StripeAccount>()), get(named<StripeAccount>()))
            }
            single<ICreateStripeAccountLinkUseCase> {
                CreateStripeAccountLinkUseCase(get(), get(named<StripeAccount>()), get(named<StripeAccount>()))
            }
            single<ICreateCheckoutSessionUseCase> { CreateCheckoutSessionUseCase(get(), get(named<StripeAccount>())) }
            single<IGetChildModelSuspendUseCase<StripeOrder, String, UUID>>(named<StripeOrder>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IStripeOrdersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<StripeOrder, CreateStripeOrderPayload, UUID>>(named<StripeOrder>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IStripeOrdersRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<StripeOrder, String, UpdateStripeOrderPayload, UUID>>(named<StripeOrder>()) {
                UpdateStripeOrderUseCase(get(), get(named<Association>()), get())
            }
            single<ICreateStripeOrderForSessionUseCase> {
                CreateStripeOrderForSessionUseCase(get(), get(named<StripeOrder>()))
            }
            single<IFulfillCheckoutItemUseCase> {
                FulfillCheckoutItemUseCase(
                    get(),
                    get(),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInUser>())
                )
            }

            // Files
            single<IListFilesUseCase> {
                ListFilesUseCase(
                    get(),
                    environment.config.property("cloudflare.bucket").getString()
                )
            }
            single<IUploadFileUseCase> {
                UploadFileUseCase(
                    get(),
                    get(),
                    environment.config.property("cloudflare.bucket").getString()
                )
            }

            // Auth
            single<IHashPasswordUseCase> { HashPasswordUseCase() }
            single<IVerifyPasswordUseCase> { VerifyPasswordUseCase() }
            single<IGetJWTPrincipalForCallUseCase> { GetJWTPrincipalForCallUseCase() }
            single<IGetSessionForCallUseCase> { GetSessionForCallUseCase() }
            single<ISetSessionForCallUseCase> { SetSessionForCallUseCase() }
            single<IClearSessionForCallUseCase> { ClearSessionForCallUseCase() }
            single<ILoginUseCase> { LoginUseCase(get(), get()) }
            single<IRegisterUseCase> { RegisterUseCase(get(), get(named<User>())) }
            single<ICreateAuthCodeUseCase> { CreateAuthCodeUseCase(get()) }
            single<IGetAuthCodeUseCase> { GetAuthCodeUseCase(get(), get(named<Client>()), get()) }
            single<IDeleteAuthCodeUseCase> { DeleteAuthCodeUseCase(get()) }
            single<IGenerateAuthTokenUseCase> { GenerateAuthTokenUseCase(get()) }
            single<IGetClientForUserForRefreshTokenUseCase> {
                GetClientForUserForRefreshTokenUseCase(get(), get(), get(named<Client>()))
            }

            // Users
            single<IGetUserUseCase> { GetUserUseCase(get()) }
            single<IGetUserForEmailUseCase> { GetUserForEmailUseCase(get()) }
            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get(), get(), get()) }
            single<IRequireUserForCallUseCase> { RequireUserForCallUseCase(get()) }
            single<IListChildModelSuspendUseCase<User, UUID>>(named<User>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<User, UUID>>(named<User>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<IGetChildModelSuspendUseCase<User, UUID, UUID>>(named<User>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<User, CreateUserPayload, UUID>>(named<User>()) {
                CreateUserUseCase(get(), get())
            }
            single<IUpdateChildModelSuspendUseCase<User, UUID, UpdateUserPayload, UUID>>(named<User>()) {
                UpdateUserUseCase(get(), get(), get())
            }
            single<IDeleteChildModelSuspendUseCase<User, UUID, UUID>>(named<User>()) {
                DeleteUserUseCase(
                    get(),
                    get(named<SubscriptionInUser>()),
                    get(named<SubscriptionInUser>()),
                    get(named<NotificationToken>()),
                    get(named<NotificationToken>()),
                    get(named<UserInRole>()),
                    get(named<UserInRole>()),
                    get(named<UserInClub>()),
                    get(named<UserInClub>())
                )
            }
            single<IListUsersLastLoggedBeforeUseCase> { ListUsersLastLoggedBeforeUseCase(get()) }
            single<IUpdateUserLastLoginUseCase> { UpdateUserLastLoginUseCase(get()) }
            single<IExportUsersAsCsvUseCase> { ExportUsersAsCsvUseCase(get(named<SubscriptionInUser>())) }

            // Subscriptions in users
            single<IListChildModelSuspendUseCase<SubscriptionInUser, UUID>>(named<SubscriptionInUser>()) {
                ListChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<IGetChildModelSuspendUseCase<SubscriptionInUser, UUID, UUID>>(named<SubscriptionInUser>()) {
                GetChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<SubscriptionInUser, CreateSubscriptionInUserPayload, UUID>>(named<SubscriptionInUser>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<SubscriptionInUser, UUID, UpdateSubscriptionInUserPayload, UUID>>(
                named<SubscriptionInUser>()
            ) {
                UpdateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<SubscriptionInUser, UUID, UUID>>(named<SubscriptionInUser>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }

            // Scans
            single<ICreateChildModelWithContextSuspendUseCase<Scan, CreateScanPayload, UUID>>(named<Scan>()) {
                CreateChildModelWithContextFromRepositorySuspendUseCase(get<IScansRepository>())
            }
            single<IListScansForDaysUseCase> { ListScansForDaysUseCase(get(), get()) }

            // Notifications
            single<ISendNotificationUseCase> { SendNotificationUseCase(get(), get()) }
            single<ISendNotificationToUserUseCase> { SendNotificationToUserUseCase(get(), get(), get()) }
            single<ISendNotificationToPermissionUseCase> { SendNotificationToPermissionUseCase(get(), get(), get()) }
            single<ISendNotificationToClubUseCase> { SendNotificationToClubUseCase(get(), get(), get()) }
            single<IListNotificationTopicsUseCase> { ListNotificationTopicsUseCase() }
            single<IListChildModelSuspendUseCase<NotificationToken, UUID>>(named<NotificationToken>()) {
                ListChildModelFromRepositorySuspendUseCase(get<INotificationTokensRepository>())
            }
            single<ICreateChildModelSuspendUseCase<NotificationToken, CreateNotificationTokenPayload, UUID>>(named<NotificationToken>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<INotificationTokensRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<NotificationToken, String, UUID>>(named<NotificationToken>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<INotificationTokensRepository>())
            }
            single<IDeleteNotificationTokenUseCase> { DeleteNotificationTokenUseCase(get()) }

            // Roles
            single<ICheckPermissionSuspendUseCase> { CheckPermissionUseCase(get()) }
            single<IGetPermissionsForUserUseCase> { GetPermissionsForUserUseCase(get()) }
            single<IListChildModelSuspendUseCase<Role, UUID>>(named<Role>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IRolesRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<Role, UUID>>(named<Role>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IRolesRepository>())
            }
            single<IGetChildModelSuspendUseCase<Role, UUID, UUID>>(named<Role>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IRolesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<Role, CreateRolePayload, UUID>>(named<Role>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IRolesRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<Role, UUID, UpdateRolePayload, UUID>>(named<Role>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IRolesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<Role, UUID, UUID>>(named<Role>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IRolesRepository>())
            }

            // Users in roles
            single<IListChildModelSuspendUseCase<UserInRole, UUID>>(named<UserInRole>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<UserInRole, UUID>>(named<UserInRole>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<IGetChildModelSuspendUseCase<UserInRole, UUID, UUID>>(named<UserInRole>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<UserInRole, CreateUserInRolePayload, UUID>>(named<UserInRole>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<UserInRole, UUID, UUID>>(named<UserInRole>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }

            // Permissions
            single<IListChildModelSuspendUseCase<PermissionInRole, UUID>>(named<PermissionInRole>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }
            single<IGetChildModelSuspendUseCase<PermissionInRole, Permission, UUID>>(named<PermissionInRole>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<PermissionInRole, CreatePermissionInRolePayload, UUID>>(named<PermissionInRole>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<PermissionInRole, Permission, UUID>>(named<PermissionInRole>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }

            // Web
            single<IListChildModelSuspendUseCase<WebPage, UUID>>(named<WebPage>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<WebPage, UUID>>(named<WebPage>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IGetWebPageByUrlUseCase> { GetWebPageByUrlUseCase(get()) }
            single<IGetHomeWebPageUseCase> { GetHomeWebPageUseCase(get()) }
            single<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>(named<WebPage>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, UUID>>(named<WebPage>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<WebPage, UUID, WebPagePayload, UUID>>(named<WebPage>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<WebPage, UUID, UUID>>(named<WebPage>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IListChildModelSuspendUseCase<WebMenu, UUID>>(named<WebMenu>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<WebMenu, UUID>>(named<WebMenu>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IGetPublicMenuForCallUseCase> { GetPublicMenuForCallUseCase(get(), get(named<WebMenu>())) }
            single<IGetRootMenuUseCase> { GetRootMenuUseCase(get()) }
            single<IGetAdminMenuForCallUseCase> {
                GetAdminMenuForCallUseCase(get(), get(), get(named<Association>()), get(), get(), get())
            }
            single<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>(named<WebMenu>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, UUID>>(named<WebMenu>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<WebMenu, UUID, UpdateWebMenuPayload, UUID>>(named<WebMenu>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<WebMenu, UUID, UUID>>(named<WebMenu>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }

            // Events
            single<IListChildModelSuspendUseCase<Event, UUID>>(named<Event>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IEventsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<Event, UUID>>(named<Event>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IEventsRepository>())
            }
            single<IGetChildModelSuspendUseCase<Event, UUID, UUID>>(named<Event>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IEventsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<Event, CreateEventPayload, UUID>>(named<Event>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IEventsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<Event, UUID, UpdateEventPayload, UUID>>(named<Event>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IEventsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<Event, UUID, UUID>>(named<Event>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IEventsRepository>())
            }
            single<IListAllEventsUseCase> { ListAllEventsUseCase(get()) }

            // Clubs
            single<IListChildModelWithContextSuspendUseCase<Club, UUID>>(named<Club>()) {
                ListChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<IListSliceChildModelWithContextSuspendUseCase<Club, UUID>>(named<Club>()) {
                ListSliceChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<IGetChildModelWithContextSuspendUseCase<Club, UUID, UUID>>(named<Club>()) {
                GetChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, UUID>>(named<Club>()) {
                CreateClubUseCase(get(), get(named<RoleInClub>()), get(named<UserInClub>()))
            }
            single<IUpdateChildModelWithContextSuspendUseCase<Club, UUID, UpdateClubPayload, UUID>>(named<Club>()) {
                UpdateChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<Club, UUID, UUID>>(named<Club>()) {
                DeleteClubUseCase(
                    get(),
                    get(named<UserInClub>()),
                    get(named<UserInClub>()),
                    get(named<RoleInClub>()),
                    get(named<RoleInClub>())
                )
            }

            // Users in clubs
            single<IListChildModelSuspendUseCase<UserInClub, UUID>>(named<UserInClub>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<UserInClub, UUID>>(named<UserInClub>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<IGetChildModelSuspendUseCase<UserInClub, UUID, UUID>>(named<UserInClub>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<UserInClub, CreateUserInClubPayload, UUID>>(named<UserInClub>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<ICreateChildModelWithContextSuspendUseCase<UserInClub, CreateUserInClubPayload, UUID>>(named<UserInClub>()) {
                CreateChildModelWithContextFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<UserInClub, UUID, UUID>>(named<UserInClub>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }

            // Roles in clubs
            single<IListChildModelSuspendUseCase<RoleInClub, UUID>>(named<RoleInClub>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<IGetChildModelSuspendUseCase<RoleInClub, UUID, UUID>>(named<RoleInClub>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, UUID>>(named<RoleInClub>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<RoleInClub, UUID, UpdateRoleInClubPayload, UUID>>(named<RoleInClub>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<RoleInClub, UUID, UUID>>(named<RoleInClub>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
        }
        val controllerModule = module {
            // Webhooks
            single<IWebhooksController> {
                WebhooksController(
                    environment.config.property("stripe.secret").getString(),
                    get(),
                    get(named<StripeOrder>()),
                )
            }

            // Associations
            single<IAssociationsController> {
                AssociationsController(
                    get(),
                    get(),
                    get(),
                    get(),
                    get(named<Association>()),
                    get(named<Association>())
                )
            }
            single<IDomainsInAssociationsController> {
                DomainsInAssociationsController(
                    get(),
                    get(),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>())
                )
            }
            single<ISubscriptionsInAssociationsController> {
                SubscriptionsInAssociationsController(
                    get(),
                    get(),
                    get(),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInAssociation>()),
                    get()
                )
            }
            single<IRootController> { RootController(get(), get()) }
            single<IDashboardController> { DashboardController(get(), get(), get(), get()) }
            single<IFilesController> { FilesController(get(), get(), get()) }
            single<INotificationsController> {
                NotificationsController(
                    get(named<Association>()),
                    get(),
                    get(),
                    get(),
                    get(),
                    get()
                )
            }

            // Auth
            single<IAuthController> {
                AuthController(
                    get(),
                    get(named<Association>()),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(named<Client>()),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(named<Association>()),
                    get(),
                    get(),
                    get()
                )
            }

            // Users
            single<IUsersController> {
                UsersController(
                    get(),
                    get(),
                    get(named<User>()),
                    get(named<User>()),
                    get(named<User>()),
                    get(named<User>()),
                    get(named<User>()),
                    get(),
                    get()
                )
            }
            single<ISubscriptionsInUsersController> {
                SubscriptionsInUsersController(
                    get(),
                    get(),
                    get(named<SubscriptionInUser>()),
                    get(named<SubscriptionInUser>()),
                    get(named<SubscriptionInUser>()),
                    get(named<SubscriptionInUser>()),
                    get()
                )
            }
            single<INotificationTokensController> {
                NotificationTokensController(
                    get(),
                    get(named<NotificationToken>())
                )
            }
            single<IScansController> {
                ScansController(
                    get(),
                    get(),
                    get(),
                    get(named<Scan>()),
                    get()
                )
            }

            // Roles
            single<IRolesController> {
                RolesController(
                    get(),
                    get(),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>())
                )
            }
            single<IUsersInRolesController> {
                UsersInRolesController(
                    get(),
                    get(),
                    get(named<UserInRole>()),
                    get(named<UserInRole>()),
                    get(named<UserInRole>()),
                    get(named<UserInRole>()),
                    get(named<UserInRole>())
                )
            }
            single<IPermissionsInRolesController> {
                PermissionsInRolesController(
                    get(),
                    get(),
                    get(named<PermissionInRole>()),
                    get(named<PermissionInRole>()),
                    get(named<PermissionInRole>()),
                    get(named<PermissionInRole>())
                )
            }

            // Web
            single<IWebPagesController> {
                WebPagesController(
                    get(),
                    get(),
                    get(named<WebPage>()),
                    get(named<WebPage>()),
                    get(),
                    get(named<WebPage>()),
                    get(named<WebPage>()),
                    get(named<WebPage>()),
                    get(named<WebPage>())
                )
            }
            single<IWebMenusController> {
                WebMenusController(
                    get(),
                    get(),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>())
                )
            }

            // Events
            single<IEventsController> {
                EventsController(
                    get(),
                    get(),
                    get(),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get()
                )
            }

            // Clubs
            single<IClubsController> {
                ClubsController(
                    get(),
                    get(),
                    get(),
                    get(named<Club>()),
                    get(named<Club>()),
                    get(named<Club>()),
                    get(named<Club>()),
                    get(named<Club>()),
                    get(named<Club>()),
                    get(named<UserInClub>()),
                    get(),
                    get()
                )
            }
            single<IUsersInClubsController> {
                UsersInClubsController(
                    get(),
                    get(),
                    get(named<UserInClub>()),
                    get(named<UserInClub>()),
                    get(named<UserInClub>()),
                    get(named<UserInClub>()),
                    get(named<UserInClub>())
                )
            }
        }
        val routerModule = module {
            single<IAssociationForCallRouter> { AssociationForCallRouter(get(), get()) }
            single { WebhooksRouter(get()) }
            single { RootRouter(get(), get(), get()) }
            single { DashboardRouter(get(), get(), get(), get(), get(), get()) }
            single { FilesRouter(get(), get(), get(), get(), get(), get()) }
            single { NotificationsRouter(get(), get(), get(), get(), get(), get()) }
            single { AssociationsRouter(get(), get(), get(), get(), get(), get()) }
            single { DomainsInAssociationsRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { SubscriptionsInAssociationsRouter(get(), get(), get(), get(), get(), get(), get(), get()) }
            single { UsersRouter(get(), get(), get(), get(), get(), get(), get(), get()) }
            single { SubscriptionsInUsersRouter(get(), get()) }
            single { NotificationTokensRouter(get(), get()) }
            single { ScansRouter(get(), get()) }
            single { AuthRouter(get(), get()) }
            single { RolesRouter(get(), get(), get(), get(), get(), get(), get(), get()) }
            single { UsersInRolesRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { PermissionsInRolesRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { WebPagesRouter(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            single { WebMenusRouter(get(), get(), get(), get(), get(), get(), get(), get()) }
            single { EventsRouter(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            single { ClubsRouter(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            single { UsersInClubsRouter(get(), get(), get(), get(), get(), get(), get()) }
        }

        modules(
            databaseModule,
            serviceModule,
            repositoryModule,
            useCaseModule,
            controllerModule,
            routerModule
        )
    }
}
