package me.nathanfallet.suitebde.plugins

import com.github.aymanizz.ktori18n.MessageResolver
import com.github.aymanizz.ktori18n.i18n
import io.ktor.server.application.*
import me.nathanfallet.ktor.routers.controllers.base.IChildModelController
import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.suitebde.controllers.associations.AssociationsController
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.DomainsInAssociationsController
import me.nathanfallet.suitebde.controllers.associations.DomainsInAssociationsRouter
import me.nathanfallet.suitebde.controllers.auth.AuthController
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.auth.IAuthController
import me.nathanfallet.suitebde.controllers.users.UsersController
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.controllers.web.IWebController
import me.nathanfallet.suitebde.controllers.web.WebController
import me.nathanfallet.suitebde.controllers.web.WebRouter
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.associations.DatabaseAssociationRepository
import me.nathanfallet.suitebde.database.associations.DatabaseDomainsInAssociationsRepository
import me.nathanfallet.suitebde.database.users.DatabaseUsersRepository
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.services.email.EmailService
import me.nathanfallet.suitebde.services.email.IEmailService
import me.nathanfallet.suitebde.usecases.application.*
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.*
import me.nathanfallet.suitebde.usecases.web.GetAdminMenuForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase
import me.nathanfallet.usecases.models.get.GetModelFromRepositorySuspendUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase
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
            single<IEmailService> {
                EmailService(
                    environment.config.property("email.host").getString(),
                    environment.config.property("email.username").getString(),
                    environment.config.property("email.password").getString()
                )
            }
        }
        val repositoryModule = module {
            single<IAssociationsRepository> { DatabaseAssociationRepository(get()) }
            single<IDomainsInAssociationsRepository> { DatabaseDomainsInAssociationsRepository(get()) }
            single<IUsersRepository> { DatabaseUsersRepository(get()) }
        }
        val useCaseModule = module {
            single<ISendEmailUseCase> { SendEmailUseCase(get()) }
            single<IExpireUseCase> { ExpireUseCase(get(), get(), get(named<Association>())) }
            single<ITranslateUseCase> { TranslateUseCase(get()) }
            single<IGetSessionForCallUseCase> { GetSessionForCallUseCase() }
            single<ISetSessionForCallUseCase> { SetSessionForCallUseCase() }

            single<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>(named<Association>()) {
                CreateAssociationUseCase(
                    get(),
                    get(named<User>())
                )
            }
            single<IGetAssociationsUseCase> { GetAssociationsUseCase(get()) }
            single<IGetModelSuspendUseCase<Association, String>>(named<Association>()) {
                GetModelFromRepositorySuspendUseCase(get<IAssociationsRepository>())
            }
            single<IGetAssociationForCallUseCase> { GetAssociationForCallUseCase(get()) }
            single<IUpdateModelSuspendUseCase<Association, String, UpdateAssociationPayload>>(named<Association>()) {
                UpdateModelFromRepositorySuspendUseCase(get<IAssociationsRepository>())
            }
            single<IDeleteModelSuspendUseCase<Association, String>>(named<Association>()) { DeleteAssociationUseCase(get()) }
            single<ICreateCodeInEmailUseCase> { CreateCodeInEmailUseCase(get(), get()) }
            single<IGetCodeInEmailUseCase> { GetCodeInEmailUseCase(get()) }
            single<IDeleteCodeInEmailUseCase> { DeleteCodeInEmailUseCase(get()) }

            single<IHashPasswordUseCase> { HashPasswordUseCase() }
            single<IVerifyPasswordUseCase> { VerifyPasswordUseCase() }
            single<ILoginUseCase> { LoginUseCase(get(), get()) }

            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get(), get(named<User>())) }
            single<IGetUsersInAssociationUseCase> { GetUsersInAssociationUseCase(get()) }
            single<IGetModelSuspendUseCase<User, String>>(named<User>()) {
                GetModelFromRepositorySuspendUseCase(get<IUsersRepository>())
            }
            single<ICreateModelSuspendUseCase<User, CreateUserPayload>>(named<User>()) {
                CreateUserUseCase(
                    get(),
                    get()
                )
            }
            single<IUpdateModelSuspendUseCase<User, String, UpdateUserPayload>>(named<User>()) {
                UpdateUserUseCase(
                    get(),
                    get()
                )
            }

            single<ICheckPermissionSuspendUseCase> { CheckPermissionUseCase() }

            single<IGetAdminMenuForCallUseCase> { GetAdminMenuForCallUseCase(get(), get(), get(), get()) }
        }
        val controllerModule = module {
            single<IWebController> { WebController() }
            single<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>(named<Association>()) {
                AssociationsController(
                    get(),
                    get(),
                    get(),
                    get(named<Association>()),
                    get(named<Association>())
                )
            }
            single<IChildModelController<DomainInAssociation, String, String, Unit, Association, String>>(named<DomainInAssociation>()) { DomainsInAssociationsController() }
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
            single<IModelController<User, String, CreateUserPayload, UpdateUserPayload>>(named<User>()) {
                UsersController(
                    get(),
                    get(),
                    get(),
                    get(),
                    get(named<User>()),
                    get(named<User>())
                )
            }
        }
        val routerModule = module {
            single { WebRouter(get()) }
            single { AssociationsRouter(get(named<Association>()), get(), get()) }
            single { DomainsInAssociationsRouter(get(named<DomainInAssociation>()), get(), get(), get()) }
            single { UsersRouter(get(named<User>()), get(), get()) }
            single { AuthRouter(get(), get()) }
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
