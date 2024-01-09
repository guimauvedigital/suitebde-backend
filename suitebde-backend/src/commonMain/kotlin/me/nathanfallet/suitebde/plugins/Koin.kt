package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import me.nathanfallet.cloudflare.client.CloudflareClient
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.i18n.usecases.localization.TranslateUseCase
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.database.IDatabase
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
import me.nathanfallet.suitebde.controllers.roles.RolesController
import me.nathanfallet.suitebde.controllers.roles.RolesRouter
import me.nathanfallet.suitebde.controllers.users.UsersController
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.controllers.web.*
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.application.ClientsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.AssociationsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.CodesInEmailsDatabaseRepository
import me.nathanfallet.suitebde.database.associations.DomainsInAssociationsDatabaseRepository
import me.nathanfallet.suitebde.database.roles.RolesDatabaseRepository
import me.nathanfallet.suitebde.database.users.ClientsInUsersDatabaseRepository
import me.nathanfallet.suitebde.database.users.UsersDatabaseRepository
import me.nathanfallet.suitebde.database.web.WebMenusDatabaseRepository
import me.nathanfallet.suitebde.database.web.WebPagesDatabaseRepository
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.associations.*
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.*
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import me.nathanfallet.suitebde.services.emails.EmailsService
import me.nathanfallet.suitebde.services.emails.IEmailsService
import me.nathanfallet.suitebde.services.jwt.IJWTService
import me.nathanfallet.suitebde.services.jwt.JWTService
import me.nathanfallet.suitebde.usecases.application.*
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.*
import me.nathanfallet.suitebde.usecases.web.*
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.create.CreateChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.DeleteChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase
import me.nathanfallet.usecases.models.get.GetChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.get.GetModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.ListChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository
import me.nathanfallet.usecases.models.repositories.IModelSuspendRepository
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase
import me.nathanfallet.usecases.models.update.UpdateChildModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.update.UpdateModelFromRepositorySuspendUseCase
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

            // Associations
            single<IAssociationsRepository> { AssociationsDatabaseRepository(get()) }
            single<ICodesInEmailsRepository> { CodesInEmailsDatabaseRepository(get()) }
            single<IDomainsInAssociationsRepository> { DomainsInAssociationsDatabaseRepository(get()) }

            // Users
            single<IUsersRepository> { UsersDatabaseRepository(get()) }
            single<IClientsInUsersRepository> { ClientsInUsersDatabaseRepository(get()) }

            // Roles
            single<IChildModelSuspendRepository<Role, String, CreateRolePayload, UpdateRolePayload, String>>(named<Role>()) {
                RolesDatabaseRepository(get())
            }

            // Web
            single<IWebPagesRepository> { WebPagesDatabaseRepository(get()) }
            single<IWebMenusRepository> { WebMenusDatabaseRepository(get()) }
        }
        val useCaseModule = module {
            // Application
            single<ISendEmailUseCase> { SendEmailUseCase(get()) }
            single<IExpireUseCase> { ExpireUseCase(get(), get(), get(), get(named<Association>())) }
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
            single<IDeleteModelSuspendUseCase<Association, String>>(named<Association>()) { DeleteAssociationUseCase(get()) }

            // Codes in emails
            single<IGetCodeInEmailUseCase> { GetCodeInEmailUseCase(get()) }
            single<ICreateCodeInEmailUseCase> { CreateCodeInEmailUseCase(get(), get()) }
            single<IDeleteCodeInEmailUseCase> { DeleteCodeInEmailUseCase(get()) }

            // Domains in associations
            single<IListChildModelSuspendUseCase<DomainInAssociation, String>>(named<DomainInAssociation>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IDomainsInAssociationsRepository>())
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
            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get(), get(), get()) }
            single<IRequireUserForCallUseCase> { RequireUserForCallUseCase(get()) }
            single<IListChildModelSuspendUseCase<User, String>>(named<User>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<IGetChildModelSuspendUseCase<User, String, String>>(named<User>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>>(named<User>()) {
                CreateUserUseCase(
                    get(),
                    get()
                )
            }
            single<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>(named<User>()) {
                UpdateUserUseCase(
                    get(),
                    get()
                )
            }

            // Roles
            single<ICheckPermissionSuspendUseCase> { CheckPermissionUseCase() }
            single<IListChildModelSuspendUseCase<Role, String>>(named<Role>()) {
                ListChildModelFromRepositorySuspendUseCase(get(named<Role>()))
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

            // Web
            single<IListChildModelSuspendUseCase<WebPage, String>>(named<WebPage>()) {
                ListChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
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
            single<IGetPublicMenuForCallUseCase> { GetPublicMenuForCallUseCase(get(), get(named<WebMenu>())) }
            single<IGetAdminMenuForCallUseCase> { GetAdminMenuForCallUseCase(get(), get(), get(), get(), get()) }
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
        }
        val controllerModule = module {
            // Associations
            single<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>(named<Association>()) {
                AssociationsController(
                    get(),
                    get(),
                    get(),
                    get(),
                    get(named<Association>()),
                    get(named<Association>())
                )
            }
            single<IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>>(
                named<DomainInAssociation>()
            ) {
                DomainsInAssociationsController(
                    get(),
                    get(),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>()),
                    get(named<DomainInAssociation>())
                )
            }

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
            single<IChildModelController<User, String, CreateUserPayload, UpdateUserPayload, Association, String>>(named<User>()) {
                UsersController(
                    get(),
                    get(),
                    get(named<User>()),
                    get(named<User>()),
                    get(named<User>())
                )
            }

            // Roles
            single<IChildModelController<Role, String, CreateRolePayload, UpdateRolePayload, Association, String>>(named<Role>()) {
                RolesController(
                    get(),
                    get(),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>()),
                    get(named<Role>())
                )
            }

            // Web
            single<IWebPagesController> {
                WebPagesController(
                    get(),
                    get(),
                    get(named<WebPage>()),
                    get(),
                    get(),
                    get(named<WebPage>()),
                    get(named<WebPage>()),
                    get(named<WebPage>()),
                    get(named<WebPage>())
                )
            }
            single<IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String>>(
                named<WebMenu>()
            ) {
                WebMenusController(
                    get(),
                    get(),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>())
                )
            }
        }
        val routerModule = module {
            single<IAssociationForCallRouter> { AssociationForCallRouter(get(), get(named<Association>())) }
            single { AssociationsRouter(get(named<Association>()), get(), get(), get()) }
            single { DomainsInAssociationsRouter(get(named<DomainInAssociation>()), get(), get(), get(), get()) }
            single { UsersRouter(get(named<User>()), get(), get(), get(), get()) }
            single { AuthRouter(get(), get()) }
            single { RolesRouter(get(named<Role>()), get(), get(), get(), get()) }
            single { WebPagesRouter(get(), get(), get(), get(), get(), get()) }
            single { WebMenusRouter(get(named<WebMenu>()), get(), get(), get(), get()) }
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
