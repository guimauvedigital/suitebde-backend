package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class SubscriptionsInAssociationsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getSubscriptionsInAssociationsUseCase: IListChildModelSuspendUseCase<SubscriptionInAssociation, String>,
    private val getSubscriptionsInAssociationsSlicedUseCase: IListSliceChildModelSuspendUseCase<SubscriptionInAssociation, String>,
    private val getSubscriptionUseCase: IGetChildModelSuspendUseCase<SubscriptionInAssociation, String, String>,
    private val createSubscriptionUseCase: ICreateChildModelSuspendUseCase<SubscriptionInAssociation, CreateSubscriptionInAssociationPayload, String>,
    private val updateSubscriptionUseCase: IUpdateChildModelSuspendUseCase<SubscriptionInAssociation, String, UpdateSubscriptionInAssociationPayload, String>,
    private val deleteSubscriptionUseCase: IDeleteChildModelSuspendUseCase<SubscriptionInAssociation, String, String>,
) : ISubscriptionsInAssociationsController {

    override suspend fun list(call: ApplicationCall, parent: Association): List<SubscriptionInAssociation> {
        if (call.request.path().contains("/admin/")) return getSubscriptionsInAssociationsUseCase(parent.id)
        return getSubscriptionsInAssociationsSlicedUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): SubscriptionInAssociation {
        return getSubscriptionUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "subscriptions_in_associations_not_found"
        )
    }

    override suspend fun create(
        call: ApplicationCall,
        parent: Association,
        payload: CreateSubscriptionInAssociationPayload,
    ): SubscriptionInAssociation {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.SUBSCRIPTIONS_CREATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "subscriptions_in_associations_update_not_allowed"
        )
        return createSubscriptionUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.SUBSCRIPTIONS_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "subscriptions_in_associations_delete_not_allowed"
        )
        val subscription = getSubscriptionUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "subscriptions_in_associations_not_found"
        )
        if (!deleteSubscriptionUseCase(subscription.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: UpdateSubscriptionInAssociationPayload,
    ): SubscriptionInAssociation {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.SUBSCRIPTIONS_UPDATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "subscriptions_in_associations_update_not_allowed"
        )
        val subscription = getSubscriptionUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "subscriptions_in_associations_not_found"
        )
        return updateSubscriptionUseCase(subscription.id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
