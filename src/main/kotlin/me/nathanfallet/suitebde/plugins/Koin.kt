package me.nathanfallet.suitebde.plugins

import com.github.aymanizz.ktori18n.MessageResolver
import com.github.aymanizz.ktori18n.i18n
import io.ktor.server.application.*
import me.nathanfallet.suitebde.controllers.associations.AssociationController
import me.nathanfallet.suitebde.controllers.associations.AssociationRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationController
import me.nathanfallet.suitebde.controllers.auth.AuthController
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.auth.IAuthController
import me.nathanfallet.suitebde.controllers.users.IUserController
import me.nathanfallet.suitebde.controllers.users.UserController
import me.nathanfallet.suitebde.controllers.users.UserRouter
import me.nathanfallet.suitebde.controllers.web.IWebController
import me.nathanfallet.suitebde.controllers.web.WebController
import me.nathanfallet.suitebde.controllers.web.WebRouter
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.associations.DatabaseAssociationRepository
import me.nathanfallet.suitebde.database.users.DatabaseUsersRepository
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.services.email.EmailService
import me.nathanfallet.suitebde.services.email.IEmailService
import me.nathanfallet.suitebde.usecases.application.*
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.*
import me.nathanfallet.suitebde.usecases.web.GetAdminMenuForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import org.koin.core.module.dsl.singleOf
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
            single<IUsersRepository> { DatabaseUsersRepository(get()) }
        }
        val useCaseModule = module {
            single<ISendEmailUseCase> { SendEmailUseCase(get()) }
            single<IExpireUseCase> { ExpireUseCase(get(), get(), get()) }
            single<ITranslateUseCase> { TranslateUseCase(get()) }
            single<IGetSessionForCallUseCase> { GetSessionForCallUseCase() }
            single<ISetSessionForCallUseCase> { SetSessionForCallUseCase() }

            single<ICreateAssociationUseCase> { CreateAssociationUseCase(get(), get()) }
            single<IGetAssociationsUseCase> { GetAssociationsUseCase(get()) }
            single<IGetAssociationForCallUseCase> { GetAssociationForCallUseCase(get()) }
            single<IDeleteAssociationUseCase> { DeleteAssociationUseCase(get()) }
            single<ICreateCodeInEmailUseCase> { CreateCodeInEmailUseCase(get(), get()) }
            single<IGetCodeInEmailUseCase> { GetCodeInEmailUseCase(get()) }
            single<IDeleteCodeInEmailUseCase> { DeleteCodeInEmailUseCase(get()) }

            single<IHashPasswordUseCase> { HashPasswordUseCase() }
            single<IVerifyPasswordUseCase> { VerifyPasswordUseCase() }
            single<ILoginUseCase> { LoginUseCase(get(), get()) }

            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get(), get()) }
            single<IGetUsersInAssociationUseCase> { GetUsersInAssociationUseCase(get()) }
            single<IGetUserUseCase> { GetUserUseCase(get()) }
            single<ICreateUserUseCase> { CreateUserUseCase(get(), get()) }
            single<IUpdateUserUseCase> { UpdateUserUseCase(get()) }

            single<ICheckPermissionUseCase> { CheckPermissionUseCase() }

            single<IGetAdminMenuForCallUseCase> { GetAdminMenuForCallUseCase(get(), get(), get(), get()) }
        }
        val controllerModule = module {
            single<IWebController> { WebController() }
            single<IAssociationController> { AssociationController(get()) }
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
                    get()
                )
            }
            single<IUserController> { UserController(get(), get(), get(), get(), get(), get()) }
        }
        val routerModule = module {
            singleOf(::WebRouter)
            singleOf(::AssociationRouter)
            singleOf(::AuthRouter)
            singleOf(::UserRouter)
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