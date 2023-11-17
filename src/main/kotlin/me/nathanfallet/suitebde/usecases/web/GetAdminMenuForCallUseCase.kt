package me.nathanfallet.suitebde.usecases.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import java.util.*

class GetAdminMenuForCallUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val translateUseCase: ITranslateUseCase
) : IGetAdminMenuForCallUseCase {

    override suspend fun invoke(input1: ApplicationCall, input2: Locale): List<WebMenu> {
        val association = getAssociationForCallUseCase(input1) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
        val user = getUserForCallUseCase(input1) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
        user.takeIf {
            checkPermissionUseCase(it, Permission.ADMIN inAssociation association)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "admin_not_allowed"
        )
        return listOf("dashboard", "users", "webpages", "webmenus")
            .filter {
                it == "dashboard" || Permission.entries.firstOrNull { p -> p.name == "${it.uppercase()}_VIEW" }
                    ?.let { permission ->
                        checkPermissionUseCase(user, permission inAssociation association)
                    } ?: true

            }
            .map {
                WebMenu(
                    it,
                    association.id,
                    translateUseCase(input2, "admin_menu_$it"),
                    if (it == "dashboard") "/admin" else "/admin/$it"
                )
            }
    }

}
