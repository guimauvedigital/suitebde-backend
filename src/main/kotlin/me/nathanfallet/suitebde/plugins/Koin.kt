package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import me.nathanfallet.suitebde.controllers.associations.AssociationController
import me.nathanfallet.suitebde.controllers.users.UserController
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.associations.DatabaseAssociationRepository
import me.nathanfallet.suitebde.database.users.DatabaseUsersRepository
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.associations.CreateAssociationUseCase
import me.nathanfallet.suitebde.usecases.associations.GetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.GetUserForCallUseCase
import me.nathanfallet.suitebde.usecases.users.HashPasswordUseCase
import me.nathanfallet.suitebde.usecases.users.LoginUseCase
import me.nathanfallet.suitebde.usecases.users.VerifyPasswordUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        val databaseModule = module {
            single {
                Database(
                    "mysql",
                    environment.config.property("database.host").getString(),
                    environment.config.property("database.name").getString(),
                    environment.config.property("database.user").getString(),
                    environment.config.property("database.password").getString()
                )
            }
        }
        val repositoryModule = module {
            single<IAssociationsRepository> {
                DatabaseAssociationRepository(get())
            }
            single<IUsersRepository> {
                DatabaseUsersRepository(get())
            }
        }
        val useCaseModule = module {
            singleOf(::CreateAssociationUseCase)
            singleOf(::GetAssociationForDomainUseCase)

            singleOf(::HashPasswordUseCase)
            singleOf(::VerifyPasswordUseCase)
            singleOf(::LoginUseCase)
            singleOf(::GetUserForCallUseCase)
        }
        val controllerModule = module {
            singleOf(::AssociationController)
            singleOf(::UserController)
        }

        modules(
            databaseModule,
            repositoryModule,
            useCaseModule,
            controllerModule
        )
    }
}