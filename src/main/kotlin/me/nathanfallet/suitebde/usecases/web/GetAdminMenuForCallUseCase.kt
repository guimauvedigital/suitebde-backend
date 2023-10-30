package me.nathanfallet.suitebde.usecases.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import java.util.*

class GetAdminMenuForCallUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionUseCase,
    private val translateUseCase: ITranslateUseCase
) : IGetAdminMenuForCallUseCase {

    override suspend fun invoke(input: Pair<ApplicationCall, Locale>): List<WebMenu> {
        val association = getAssociationForCallUseCase(input.first) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
        val user = getUserForCallUseCase(input.first) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
        user.takeIf {
            checkPermissionUseCase(it, association, Permission.ADMIN)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "admin_not_allowed"
        )
        return listOf("dashboard", "users")
            .filter {
                it == "dashboard" ||
                        checkPermissionUseCase(
                            user, association,
                            Permission.valueOf("${it.uppercase()}_VIEW")
                        )
            }
            .map {
                WebMenu(
                    it,
                    association.id,
                    translateUseCase(input.second, "admin_menu_$it"),
                    if (it == "dashboard") "/admin" else "/admin/$it"
                )
            }
    }

}