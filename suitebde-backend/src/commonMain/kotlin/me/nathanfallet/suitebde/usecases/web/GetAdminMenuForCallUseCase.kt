package me.nathanfallet.suitebde.usecases.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class GetAdminMenuForCallUseCase(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getAssociationByIdUseCase: IGetModelSuspendUseCase<Association, String>,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase,
) : IGetAdminMenuForCallUseCase {

    private fun adminAssociationMenu(input: ApplicationCall) = listOf(
        WebMenu(
            "associations",
            "admin",
            translateUseCase(getLocaleForCallUseCase(input), "admin_menu_associations"),
            "/admin/associations"
        )
    )

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> {
        val user = requireUserForCallUseCase(input) as User
        val association = getAssociationForCallUseCase(input)
            ?: getAssociationByIdUseCase(user.associationId)
            ?: if (checkPermissionUseCase(user, AdminPermission)) return adminAssociationMenu(input)
            else throw ControllerException(HttpStatusCode.NotFound, "associations_not_found")

        user.takeIf {
            checkPermissionUseCase(it, Permission.ADMIN inAssociation association.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "admin_not_allowed"
        )

        val locale = getLocaleForCallUseCase(input)
        return listOf(
            "dashboard",
            "subscriptions",
            "users",
            "roles",
            "webpages",
            "webmenus",
            "events",
            "clubs",
            "notifications"
        )
            .filter {
                it == "dashboard" || it.permission?.let { permission ->
                    checkPermissionUseCase(user, permission inAssociation association.id)
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

    private val String.permission: Permission?
        get() = Permission.entries.firstOrNull { p -> p.name == "${uppercase()}_VIEW" }
            ?: Permission.entries.firstOrNull { p -> p.name == "${uppercase()}_SEND" }

}
