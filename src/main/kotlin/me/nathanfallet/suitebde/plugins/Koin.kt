package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.associations.DatabaseAssociationRepository
import me.nathanfallet.suitebde.database.users.DatabaseUsersRepository
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.associations.CreateAssociationUseCase
import me.nathanfallet.suitebde.usecases.users.HashPasswordUseCase
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
            singleOf(::HashPasswordUseCase)
        }

        modules(
            databaseModule,
            repositoryModule,
            useCaseModule
        )
    }
}