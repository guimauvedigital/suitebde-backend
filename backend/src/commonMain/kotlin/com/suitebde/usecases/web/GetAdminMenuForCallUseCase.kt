package com.suitebde.usecases.web

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.AdminPermission
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.models.web.WebMenu
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*

class GetAdminMenuForCallUseCase(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getAssociationByIdUseCase: IGetModelSuspendUseCase<Association, UUID>,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase,
) : IGetAdminMenuForCallUseCase {

    private fun adminAssociationMenu(input: ApplicationCall) = listOf(
        WebMenu(
            UUID(),
            UUID(),
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
            "files",
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
                    UUID(),
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
