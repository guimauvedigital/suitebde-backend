package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import me.nathanfallet.cloudflare.client.CloudflareClient
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.i18n.usecases.localization.TranslateUseCase
import me.nathanfallet.ktorx.database.sessions.SessionsDatabaseRepository
import me.nathanfallet.ktorx.repositories.sessions.ISessionsRepository
import me.nathanfallet.ktorx.usecases.auth.*
import me.nathanfallet.ktorx.usecases.localization.GetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.ktorx.usecases.users.RequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.*
import me.nathanfallet.suitebde.controllers.auth.AuthController
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.auth.IAuthController
import me.nathanfallet.suitebde.controllers.clubs.*
import me.nathanfallet.suitebde.controllers.dashboard.DashboardController
import me.nathanfallet.suitebde.controllers.dashboard.DashboardRouter
import me.nathanfallet.suitebde.controllers.dashboard.IDashboardController
import me.nathanfallet.suitebde.controllers.events.EventsController
import me.nathanfallet.suitebde.controllers.events.EventsRouter
import me.nathanfallet.suitebde.controllers.events.IEventsController
import me.nathanfallet.suitebde.controllers.roles.*
import me.nathanfallet.suitebde.controllers.root.IRootController
import me.nathanfallet.suitebde.controllers.root.RootController
import me.nathanfallet.suitebde.controllers.root.RootRouter
import me.nathanfallet.suitebde.controllers.users.*
import me.nathanfallet.suitebde.controllers.web.*
import me.nathanfallet.suitebde.controllers.webhooks.IWebhooksController
import me.nathanfallet.suitebde.controllers.webhooks.WebhooksController
import me.nathanfallet.suitebde.controllers.webhooks.WebhooksRouter
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.application.ClientsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.AssociationsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.CodesInEmailsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.DomainsInAssociationsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.SubscriptionsInAssociationsDatabaseRepository
import me.nathanfallet.suitebde.database.clubs.ClubsDatabaseRepository
import me.nathanfallet.suitebde.database.clubs.RolesInClubsDatabaseRepository
import me.nathanfallet.suitebde.database.clubs.UsersInClubsDatabaseRepository
import me.nathanfallet.suitebde.database.events.EventsDatabaseRepository
import me.nathanfallet.suitebde.database.roles.PermissionsInRolesDatabaseRepository
import me.nathanfallet.suitebde.database.roles.RolesDatabaseRepository
import me.nathanfallet.suitebde.database.roles.UsersInRolesDatabaseRepository
import me.nathanfallet.suitebde.database.stripe.StripeAccountsDatabaseRepository
import me.nathanfallet.suitebde.database.stripe.StripeOrdersDatabaseRepository
import me.nathanfallet.suitebde.database.users.ClientsInUsersDatabaseRepository
import me.nathanfallet.suitebde.database.users.SubscriptionsInUsersDatabaseRepository
import me.nathanfallet.suitebde.database.users.UsersDatabaseRepository
import me.nathanfallet.suitebde.database.web.WebMenusDatabaseRepository
import me.nathanfallet.suitebde.database.web.WebPagesDatabaseRepository
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.associations.*
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.models.clubs.*
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.models.roles.*
import me.nathanfallet.suitebde.models.stripe.*
import me.nathanfallet.suitebde.models.users.*
import me.nathanfallet.suitebde.models.web.*
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.ISubscriptionsInAssociationsRepository
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.suitebde.repositories.clubs.IRolesInClubsRepository
import me.nathanfallet.suitebde.repositories.clubs.IUsersInClubsRepository
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInRolesRepository
import me.nathanfallet.suitebde.repositories.roles.IUsersInRolesRepository
import me.nathanfallet.suitebde.repositories.stripe.IStripeAccountsRepository
import me.nathanfallet.suitebde.repositories.stripe.IStripeOrdersRepository
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.repositories.users.ISubscriptionsInUsersRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import me.nathanfallet.suitebde.services.emails.EmailsService
import me.nathanfallet.suitebde.services.emails.IEmailsService
import me.nathanfallet.suitebde.services.jwt.IJWTService
import me.nathanfallet.suitebde.services.jwt.JWTService
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.suitebde.services.stripe.StripeService
import me.nathanfallet.suitebde.usecases.application.*
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.clubs.CreateClubUseCase
import me.nathanfallet.suitebde.usecases.clubs.DeleteClubUseCase
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.roles.GetPermissionsForUserUseCase
import me.nathanfallet.suitebde.usecases.roles.IGetPermissionsForUserUseCase
import me.nathanfallet.suitebde.usecases.stripe.*
import me.nathanfallet.suitebde.usecases.users.*
import me.nathanfallet.suitebde.usecases.web.*
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.create.CreateChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import me.nathanfallet.usecases.models.create.context.CreateChildModelWithContextFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.delete.DeleteChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase
import me.nathanfallet.usecases.models.get.GetChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.get.GetModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.get.context.GetChildModelWithContextFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.get.context.IGetChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.ListChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.list.context.IListChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.context.ListChildModelWithContextFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.ListSliceChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.list.slice.context.IListSliceChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.context.ListSliceChildModelWithContextFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository
import me.nathanfallet.usecases.models.repositories.IModelSuspendRepository
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase
import me.nathanfallet.usecases.models.update.UpdateChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.update.UpdateModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.update.context.IUpdateChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.update.context.UpdateChildModelWithContextFromRepositorySuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
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
        }
        val repositoryModule = module {
            // Application
            single<IModelSuspendRepository<Client, String, Unit, Unit>>(named<Client>()) {
                ClientsDatabaseRepository(get())
            }
            single<ISessionsRepository> { SessionsDatabaseRepository(get()) }
            single<IAddDurationUseCase> { AddDurationUseCase() }

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

            // Roles
            single<IChildModelSuspendRepository<Role, String, CreateRolePayload, UpdateRolePayload, String>>(named<Role>()) {
                RolesDatabaseRepository(get())
            }
            single<IPermissionsInRolesRepository> { PermissionsInRolesDatabaseRepository(get()) }
            single<IUsersInRolesRepository> { UsersInRolesDatabaseRepository(get()) }

            // Web
            single<IWebPagesRepository> { WebPagesDatabaseRepository(get()) }
            single<IWebMenusRepository> { WebMenusDatabaseRepository(get()) }

            // Events
            single<IChildModelSuspendRepository<Event, String, CreateEventPayload, UpdateEventPayload, String>>(named<Event>()) {
                EventsDatabaseRepository(get())
            }

            // Clubs
            single<IClubsRepository> { ClubsDatabaseRepository(get()) }
            single<IRolesInClubsRepository> { RolesInClubsDatabaseRepository(get()) }
            single<IUsersInClubsRepository> { UsersInClubsDatabaseRepository(get(), get()) }
        }
        val useCaseModule = module {
            // Application
            single<ISendEmailUseCase> { SendEmailUseCase(get()) }
            single<IExpireUseCase> { ExpireUseCase(get(), get()) }
            single<ITranslateUseCase> { TranslateUseCase() }
            single<IGetLocaleForCallUseCase> { GetLocaleForCallUseCase() }
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
            single<IGetModelSuspendUseCase<Client, String>>(named<Client>()) {
                GetModelFromRepositorySuspendUseCase(get(named<Client>()))
            }

            // Associations
            single<IGetAssociationsUseCase> { GetAssociationsUseCase(get()) }
            single<IGetAssociationForCallUseCase> { GetAssociationForCallUseCase(get()) }
            single<IRequireAssociationForCallUseCase> { RequireAssociationForCallUseCase(get()) }
            single<IGetModelSuspendUseCase<Association, String>>(named<Association>()) {
                GetModelFromRepositorySuspendUseCase(get<IAssociationsRepository>())
            }
            single<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>(named<Association>()) {
                CreateAssociationUseCase(
                    get(),
                    get(named<User>())
                )
            }
            single<IUpdateModelSuspendUseCase<Association, String, UpdateAssociationPayload>>(named<Association>()) {
                UpdateModelFromRepositorySuspendUseCase(get<IAssociationsRepository>())
            }
            single<IDeleteModelSuspendUseCase<Association, String>>(named<Association>()) {
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
            single<IListChildModelSuspendUseCase<DomainInAssociation, String>>(named<DomainInAssociation>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<DomainInAssociation, String>>(named<DomainInAssociation>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
            }
            single<IGetChildModelSuspendUseCase<DomainInAssociation, String, String>>(named<DomainInAssociation>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, String>>(named<DomainInAssociation>()) {
                CreateDomainInAssociationUseCase(get(), get())
            }
            single<IDeleteChildModelSuspendUseCase<DomainInAssociation, String, String>>(named<DomainInAssociation>()) {
                DeleteDomainInAssociationUseCase(get(), get())
            }

            // Subscriptions in associations
            single<IListChildModelSuspendUseCase<SubscriptionInAssociation, String>>(named<SubscriptionInAssociation>()) {
                ListChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<SubscriptionInAssociation, String>>(named<SubscriptionInAssociation>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IGetChildModelSuspendUseCase<SubscriptionInAssociation, String, String>>(named<SubscriptionInAssociation>()) {
                GetChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<SubscriptionInAssociation, CreateSubscriptionInAssociationPayload, String>>(
                named<SubscriptionInAssociation>()
            ) {
                CreateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<SubscriptionInAssociation, String, String>>(named<SubscriptionInAssociation>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<SubscriptionInAssociation, String, UpdateSubscriptionInAssociationPayload, String>>(
                named<SubscriptionInAssociation>()
            ) {
                UpdateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInAssociationsRepository>())
            }

            // Stripe
            single<IHasStripeAccountLinkedUseCase> { HasStripeAccountLinkedUseCase(get()) }
            single<IListChildModelSuspendUseCase<StripeAccount, String>>(named<StripeAccount>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<IGetChildModelSuspendUseCase<StripeAccount, String, String>>(named<StripeAccount>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<StripeAccount, CreateStripeAccountPayload, String>>(
                named<StripeAccount>()
            ) {
                CreateChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<StripeAccount, String, UpdateStripeAccountPayload, String>>(
                named<StripeAccount>()
            ) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IStripeAccountsRepository>())
            }
            single<IRefreshStripeAccountUseCase> {
                RefreshStripeAccountUseCase(
                    get(),
                    get(named<StripeAccount>()),
                    get(named<StripeAccount>())
                )
            }
            single<ICreateStripeAccountLinkUseCase> {
                CreateStripeAccountLinkUseCase(
                    get(),
                    get(named<StripeAccount>()),
                    get(named<StripeAccount>())
                )
            }
            single<ICreateCheckoutSessionUseCase> {
                CreateCheckoutSessionUseCase(
                    get(),
                    get(named<StripeAccount>())
                )
            }
            single<IGetChildModelSuspendUseCase<StripeOrder, String, String>>(named<StripeOrder>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IStripeOrdersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<StripeOrder, CreateStripeOrderPayload, String>>(named<StripeOrder>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IStripeOrdersRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<StripeOrder, String, UpdateStripeOrderPayload, String>>(named<StripeOrder>()) {
                UpdateStripeOrderUseCase(get(), get(named<Association>()), get())
            }
            single<ICreateStripeOrderForSessionUseCase> {
                CreateStripeOrderForSessionUseCase(
                    get(),
                    get(named<StripeOrder>())
                )
            }
            single<IFulfillCheckoutItemUseCase> {
                FulfillCheckoutItemUseCase(
                    get(),
                    get(),
                    get(named<SubscriptionInAssociation>()),
                    get(named<SubscriptionInUser>())
                )
            }

            // Auth
            single<IHashPasswordUseCase> { HashPasswordUseCase() }
            single<IVerifyPasswordUseCase> { VerifyPasswordUseCase() }
            single<IGetJWTPrincipalForCallUseCase> { GetJWTPrincipalForCallUseCase() }
            single<ICreateSessionForUserUseCase> { CreateSessionForUserUseCase() }
            single<IGetSessionForCallUseCase> { GetSessionForCallUseCase() }
            single<ISetSessionForCallUseCase> { SetSessionForCallUseCase() }
            single<ILoginUseCase<LoginPayload>> { LoginUseCase(get(), get()) }
            single<IRegisterUseCase<RegisterCodePayload>> { RegisterUseCase(get(), get(named<User>())) }
            single<ICreateCodeRegisterUseCase<RegisterPayload>> {
                CreateCodeRegisterUseCase(
                    get(),
                    get(),
                    get(),
                    get(),
                    get()
                )
            }
            single<IGetCodeRegisterUseCase<RegisterPayload>> { GetCodeRegisterUseCase(get(), get()) }
            single<IDeleteCodeRegisterUseCase> { DeleteCodeRegisterUseCase(get()) }
            single<IGetClientUseCase> { GetClientFromModelUseCase<Client>(get(named<Client>())) }
            single<ICreateAuthCodeUseCase> { CreateAuthCodeUseCase(get()) }
            single<IGetAuthCodeUseCase> { GetAuthCodeUseCase(get(), get(), get()) }
            single<IDeleteAuthCodeUseCase> { DeleteAuthCodeUseCase(get()) }
            single<IGenerateAuthTokenUseCase> {
                GenerateAuthTokenUseCase(get())
            }

            // Users
            single<IGetUserUseCase> { GetUserUseCase(get()) }
            single<IGetUserForEmailUseCase> { GetUserForEmailUseCase(get()) }
            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get(), get(), get()) }
            single<IRequireUserForCallUseCase> { RequireUserForCallUseCase(get()) }
            single<IListChildModelSuspendUseCase<User, String>>(named<User>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<User, String>>(named<User>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<IGetChildModelSuspendUseCase<User, String, String>>(named<User>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>>(named<User>()) {
                CreateUserUseCase(get(), get())
            }
            single<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>(named<User>()) {
                UpdateUserUseCase(get(), get(), get())
            }

            // Subscriptions in users
            single<IListChildModelSuspendUseCase<SubscriptionInUser, String>>(named<SubscriptionInUser>()) {
                ListChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<IGetChildModelSuspendUseCase<SubscriptionInUser, String, String>>(named<SubscriptionInUser>()) {
                GetChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<SubscriptionInUser, CreateSubscriptionInUserPayload, String>>(named<SubscriptionInUser>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<SubscriptionInUser, String, UpdateSubscriptionInUserPayload, String>>(
                named<SubscriptionInUser>()
            ) {
                UpdateChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<SubscriptionInUser, String, String>>(named<SubscriptionInUser>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<ISubscriptionsInUsersRepository>())
            }

            // Roles
            single<ICheckPermissionSuspendUseCase> { CheckPermissionUseCase(get()) }
            single<IGetPermissionsForUserUseCase> { GetPermissionsForUserUseCase(get()) }
            single<IListChildModelSuspendUseCase<Role, String>>(named<Role>()) {
                ListChildModelFromRepositorySuspendUseCase(get(named<Role>()))
            }
            single<IListSliceChildModelSuspendUseCase<Role, String>>(named<Role>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get(named<Role>()))
            }
            single<IGetChildModelSuspendUseCase<Role, String, String>>(named<Role>()) {
                GetChildModelFromRepositorySuspendUseCase(get(named<Role>()))
            }
            single<ICreateChildModelSuspendUseCase<Role, CreateRolePayload, String>>(named<Role>()) {
                CreateChildModelFromRepositorySuspendUseCase(get(named<Role>()))
            }
            single<IUpdateChildModelSuspendUseCase<Role, String, UpdateRolePayload, String>>(named<Role>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get(named<Role>()))
            }
            single<IDeleteChildModelSuspendUseCase<Role, String, String>>(named<Role>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get(named<Role>()))
            }

            // Users in roles
            single<IListChildModelSuspendUseCase<UserInRole, String>>(named<UserInRole>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<UserInRole, String>>(named<UserInRole>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<IGetChildModelSuspendUseCase<UserInRole, String, String>>(named<UserInRole>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<UserInRole, CreateUserInRolePayload, String>>(named<UserInRole>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<UserInRole, String, String>>(named<UserInRole>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IUsersInRolesRepository>())
            }

            // Permissions
            single<IListChildModelSuspendUseCase<PermissionInRole, String>>(named<PermissionInRole>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }
            single<IGetChildModelSuspendUseCase<PermissionInRole, String, String>>(named<PermissionInRole>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<PermissionInRole, CreatePermissionInRolePayload, String>>(named<PermissionInRole>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<PermissionInRole, String, String>>(named<PermissionInRole>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IPermissionsInRolesRepository>())
            }

            // Web
            single<IListChildModelSuspendUseCase<WebPage, String>>(named<WebPage>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<WebPage, String>>(named<WebPage>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IGetWebPageByUrlUseCase> { GetWebPageByUrlUseCase(get()) }
            single<IGetHomeWebPageUseCase> { GetHomeWebPageUseCase(get()) }
            single<IGetChildModelSuspendUseCase<WebPage, String, String>>(named<WebPage>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, String>>(named<WebPage>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<WebPage, String, WebPagePayload, String>>(named<WebPage>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<WebPage, String, String>>(named<WebPage>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IListChildModelSuspendUseCase<WebMenu, String>>(named<WebMenu>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<WebMenu, String>>(named<WebMenu>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IGetPublicMenuForCallUseCase> { GetPublicMenuForCallUseCase(get(), get(named<WebMenu>())) }
            single<IGetRootMenuUseCase> { GetRootMenuUseCase(get()) }
            single<IGetAdminMenuForCallUseCase> {
                GetAdminMenuForCallUseCase(get(), get(), get(named<Association>()), get(), get(), get())
            }
            single<IGetChildModelSuspendUseCase<WebMenu, String, String>>(named<WebMenu>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, String>>(named<WebMenu>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<WebMenu, String, UpdateWebMenuPayload, String>>(named<WebMenu>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<WebMenu, String, String>>(named<WebMenu>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IWebMenusRepository>())
            }

            // Events
            single<IListChildModelSuspendUseCase<Event, String>>(named<Event>()) {
                ListChildModelFromRepositorySuspendUseCase(get(named<Event>()))
            }
            single<IListSliceChildModelSuspendUseCase<Event, String>>(named<Event>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get(named<Event>()))
            }
            single<IGetChildModelSuspendUseCase<Event, String, String>>(named<Event>()) {
                GetChildModelFromRepositorySuspendUseCase(get(named<Event>()))
            }
            single<ICreateChildModelSuspendUseCase<Event, CreateEventPayload, String>>(named<Event>()) {
                CreateChildModelFromRepositorySuspendUseCase(get(named<Event>()))
            }
            single<IUpdateChildModelSuspendUseCase<Event, String, UpdateEventPayload, String>>(named<Event>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get(named<Event>()))
            }
            single<IDeleteChildModelSuspendUseCase<Event, String, String>>(named<Event>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get(named<Event>()))
            }

            // Clubs
            single<IListChildModelWithContextSuspendUseCase<Club, String>>(named<Club>()) {
                ListChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<IListSliceChildModelWithContextSuspendUseCase<Club, String>>(named<Club>()) {
                ListSliceChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<IGetChildModelWithContextSuspendUseCase<Club, String, String>>(named<Club>()) {
                GetChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, String>>(named<Club>()) {
                CreateClubUseCase(get(), get(named<RoleInClub>()), get(named<UserInClub>()))
            }
            single<IUpdateChildModelWithContextSuspendUseCase<Club, String, UpdateClubPayload, String>>(named<Club>()) {
                UpdateChildModelWithContextFromRepositorySuspendUseCase(get<IClubsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<Club, String, String>>(named<Club>()) {
                DeleteClubUseCase(
                    get(),
                    get(named<UserInClub>()),
                    get(named<UserInClub>()),
                    get(named<RoleInClub>()),
                    get(named<RoleInClub>())
                )
            }

            // Users in clubs
            single<IListChildModelSuspendUseCase<UserInClub, String>>(named<UserInClub>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<IListSliceChildModelSuspendUseCase<UserInClub, String>>(named<UserInClub>()) {
                ListSliceChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<IGetChildModelSuspendUseCase<UserInClub, String, String>>(named<UserInClub>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<UserInClub, CreateUserInClubPayload, String>>(named<UserInClub>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<ICreateChildModelWithContextSuspendUseCase<UserInClub, CreateUserInClubPayload, String>>(named<UserInClub>()) {
                CreateChildModelWithContextFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<UserInClub, String, String>>(named<UserInClub>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IUsersInClubsRepository>())
            }

            // Roles in clubs
            single<IListChildModelSuspendUseCase<RoleInClub, String>>(named<RoleInClub>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<IGetChildModelSuspendUseCase<RoleInClub, String, String>>(named<RoleInClub>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, String>>(named<RoleInClub>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<RoleInClub, String, UpdateRoleInClubPayload, String>>(named<RoleInClub>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IRolesInClubsRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<RoleInClub, String, String>>(named<RoleInClub>()) {
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

            // Auth
            single<IAuthController> {
                AuthController(
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
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
                    get()
                )
            }
            single<ISubscriptionsInUsersController> {
                SubscriptionsInUsersController(
                    get(),
                    get(),
                    get(named<SubscriptionInUser>())
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
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>()),
                    get(named<Event>())
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
                    get(named<Club>())
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
            single { DashboardRouter(get(), get(), get(), get(), get()) }
            single { AssociationsRouter(get(), get(), get(), get(), get()) }
            single { DomainsInAssociationsRouter(get(), get(), get(), get(), get(), get()) }
            single { SubscriptionsInAssociationsRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { UsersRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { SubscriptionsInUsersRouter(get(), get()) }
            single { AuthRouter(get(), get()) }
            single { RolesRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { UsersInRolesRouter(get(), get()) }
            single { PermissionsInRolesRouter(get(), get()) }
            single { WebPagesRouter(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            single { WebMenusRouter(get(), get(), get(), get(), get(), get(), get()) }
            single { EventsRouter(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            single { ClubsRouter(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            single { UsersInClubsRouter(get(), get()) }
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
