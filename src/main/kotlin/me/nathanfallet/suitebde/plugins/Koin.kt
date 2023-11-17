package me.nathanfallet.suitebde.plugins

import com.github.aymanizz.ktori18n.MessageResolver
import com.github.aymanizz.ktori18n.i18n
import io.ktor.server.application.*
import me.nathanfallet.cloudflare.client.CloudflareClient
import me.nathanfallet.cloudflare.client.ICloudflareClient
import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.ktorx.controllers.base.IModelController
import me.nathanfallet.suitebde.controllers.associations.*
import me.nathanfallet.suitebde.controllers.auth.AuthController
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.auth.IAuthController
import me.nathanfallet.suitebde.controllers.users.UsersController
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.controllers.web.*
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.associations.DatabaseAssociationRepository
import me.nathanfallet.suitebde.database.associations.DatabaseDomainsInAssociationsRepository
import me.nathanfallet.suitebde.database.users.DatabaseUsersRepository
import me.nathanfallet.suitebde.database.web.DatabaseWebMenusRepository
import me.nathanfallet.suitebde.database.web.DatabaseWebPagesRepository
import me.nathanfallet.suitebde.models.associations.*
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.*
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import me.nathanfallet.suitebde.services.emails.EmailsService
import me.nathanfallet.suitebde.services.emails.IEmailsService
import me.nathanfallet.suitebde.usecases.application.*
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.*
import me.nathanfallet.suitebde.usecases.web.*
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
        val i18nModule = module {
            single<MessageResolver> { this@configureKoin.i18n }
        }
        val databaseModule = module {
            single {
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
            single<ICloudflareClient> {
                CloudflareClient(
                    environment.config.property("cloudflare.token").getString()
                )
            }
        }
        val repositoryModule = module {
            single<IAssociationsRepository> { DatabaseAssociationRepository(get()) }
            single<IDomainsInAssociationsRepository> { DatabaseDomainsInAssociationsRepository(get()) }
            single<IUsersRepository> { DatabaseUsersRepository(get()) }
            single<IWebPagesRepository> { DatabaseWebPagesRepository(get()) }
            single<IWebMenusRepository> { DatabaseWebMenusRepository(get()) }
        }
        val useCaseModule = module {
            // Application
            single<ISendEmailUseCase> { SendEmailUseCase(get()) }
            single<IExpireUseCase> { ExpireUseCase(get(), get(), get(named<Association>())) }
            single<ITranslateUseCase> { TranslateUseCase(get()) }
            single<IGetSessionForCallUseCase> { GetSessionForCallUseCase() }
            single<ISetSessionForCallUseCase> { SetSessionForCallUseCase() }
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
            single<IGetDomainsInAssociationsUseCase> { GetDomainsInAssociationsUseCase(get()) }
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
            single<ILoginUseCase> { LoginUseCase(get(), get()) }

            // Users
            single<IGetUserUseCase> { GetUserUseCase(get()) }
            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get(), get()) }
            single<IRequireUserForCallUseCase> { RequireUserForCallUseCase(get()) }
            single<IGetUsersInAssociationUseCase> { GetUsersInAssociationUseCase(get()) }
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

            // Web
            single<IGetWebPagesUseCase> { GetWebPagesUseCase(get()) }
            single<IGetWebPageByUrlUseCase> { GetWebPageByUrlUseCase(get()) }
            single<IGetHomeWebPageUseCase> { GetHomeWebPageUseCase(get()) }
            single<IGetChildModelSuspendUseCase<WebPage, String, String>>(named<WebPage>()) {
                GetChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<ICreateChildModelSuspendUseCase<WebPage, CreateWebPagePayload, String>>(named<WebPage>()) {
                CreateChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IUpdateChildModelSuspendUseCase<WebPage, String, UpdateWebPagePayload, String>>(named<WebPage>()) {
                UpdateChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IDeleteChildModelSuspendUseCase<WebPage, String, String>>(named<WebPage>()) {
                DeleteChildModelFromRepositorySuspendUseCase(get<IWebPagesRepository>())
            }
            single<IGetWebMenusUseCase> { GetWebMenusUseCase(get()) }
            single<IGetPublicMenuForCallUseCase> { GetPublicMenuForCallUseCase(get(), get()) }
            single<IGetAdminMenuForCallUseCase> { GetAdminMenuForCallUseCase(get(), get(), get(), get()) }
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
                    get(),
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
                    get(named<User>()),
                    get(named<Association>()),
                    get(),
                    get()
                )
            }

            // Users
            single<IChildModelController<User, String, CreateUserPayload, UpdateUserPayload, Association, String>>(named<User>()) {
                UsersController(
                    get(),
                    get(),
                    get(),
                    get(named<User>()),
                    get(named<User>())
                )
            }

            // Web
            single<IWebPagesController> {
                WebPagesController(
                    get(),
                    get(),
                    get(),
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
                    get(),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>()),
                    get(named<WebMenu>())
                )
            }
        }
        val routerModule = module {
            single<IAssociationForCallRouter> { AssociationForCallRouter(get(), get(named<Association>())) }
            single { AssociationsRouter(get(named<Association>()), get(), get()) }
            single { DomainsInAssociationsRouter(get(named<DomainInAssociation>()), get(), get(), get()) }
            single { UsersRouter(get(named<User>()), get(), get(), get()) }
            single { AuthRouter(get(), get()) }
            single { WebPagesRouter(get(), get(), get(), get(), get()) }
            single { WebMenusRouter(get(named<WebMenu>()), get(), get(), get()) }
        }

        modules(
            i18nModule,
            databaseModule,
            serviceModule,
            repositoryModule,
            useCaseModule,
            controllerModule,
            routerModule
        )
    }
}
