package me.nathanfallet.suitebde.plugins

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
import me.nathanfallet.suitebde.usecases.application.ISendEmailUseCase
import me.nathanfallet.suitebde.usecases.application.SendEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
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
        val repositoryModule = module {
            single<IAssociationsRepository> { DatabaseAssociationRepository(get()) }
            single<IUsersRepository> { DatabaseUsersRepository(get()) }
        }
        val useCaseModule = module {
            single<ISendEmailUseCase> { SendEmailUseCase() }

            single<ICreateAssociationUseCase> { CreateAssociationUseCase(get(), get(), get()) }
            single<IGetAssociationsUseCase> { GetAssociationsUseCase(get()) }
            single<IGetAssociationForCallUseCase> { GetAssociationForCallUseCase(get()) }
            single<ICreateCodeInEmailUseCase> { CreateCodeInEmailUseCase(get(), get()) }
            single<IGetCodeInEmailUseCase> { GetCodeInEmailUseCase(get()) }
            single<IDeleteCodeInEmailUseCase> { DeleteCodeInEmailUseCase(get()) }

            single<IHashPasswordUseCase> { HashPasswordUseCase() }
            single<IVerifyPasswordUseCase> { VerifyPasswordUseCase() }
            single<ILoginUseCase> { LoginUseCase(get(), get()) }

            single<IGetUserForCallUseCase> { GetUserForCallUseCase(get()) }
            single<IGetUsersInAssociationUseCase> { GetUsersInAssociationUseCase(get()) }
            single<IGetUserUseCase> { GetUserUseCase(get()) }
            single<IUpdateUserUseCase> { UpdateUserUseCase(get()) }

            single<ICheckPermissionUseCase> { CheckPermissionUseCase() }
        }
        val controllerModule = module {
            single<IWebController> { WebController() }
            single<IAssociationController> { AssociationController(get()) }
            single<IAuthController> { AuthController(get(), get(), get(), get(), get(), get()) }
            single<IUserController> { UserController(get(), get(), get(), get(), get(), get()) }
        }
        val routerModule = module {
            singleOf(::WebRouter)
            singleOf(::AssociationRouter)
            singleOf(::AuthRouter)
            singleOf(::UserRouter)
        }

        modules(
            databaseModule,
            repositoryModule,
            useCaseModule,
            controllerModule,
            routerModule
        )
    }
}