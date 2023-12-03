package me.nathanfallet.suitebde.usecases.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class GetAdminMenuForCallUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase
) : IGetAdminMenuForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> {
        val association = getAssociationForCallUseCase(input) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
        val user = getUserForCallUseCase(input) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
        user.takeIf {
            checkPermissionUseCase(it, Permission.ADMIN inAssociation association)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "admin_not_allowed"
        )
        val locale = getLocaleForCallUseCase(input)
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
                    translateUseCase(locale, "admin_menu_$it"),
                    if (it == "dashboard") "/admin" else "/admin/$it"
                )
            }
    }

}
