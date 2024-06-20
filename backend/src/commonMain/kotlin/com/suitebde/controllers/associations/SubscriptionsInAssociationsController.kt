package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.stripe.CheckoutItem
import com.suitebde.models.stripe.CheckoutSession
import com.suitebde.models.users.User
import com.suitebde.usecases.stripe.ICreateCheckoutSessionUseCase
import com.suitebde.usecases.stripe.IHasStripeAccountLinkedUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class SubscriptionsInAssociationsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val hasStripeAccountLinkedUseCase: IHasStripeAccountLinkedUseCase,
    private val getSubscriptionsInAssociationsUseCase: IListChildModelSuspendUseCase<SubscriptionInAssociation, UUID>,
    private val getSubscriptionsInAssociationsSlicedUseCase: IListSliceChildModelSuspendUseCase<SubscriptionInAssociation, UUID>,
    private val getSubscriptionUseCase: IGetChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UUID>,
    private val createSubscriptionUseCase: ICreateChildModelSuspendUseCase<SubscriptionInAssociation, CreateSubscriptionInAssociationPayload, UUID>,
    private val updateSubscriptionUseCase: IUpdateChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UpdateSubscriptionInAssociationPayload, UUID>,
    private val deleteSubscriptionUseCase: IDeleteChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UUID>,
    private val createCheckoutSessionUseCase: ICreateCheckoutSessionUseCase,
) : ISubscriptionsInAssociationsController {

    override suspend fun list(
        call: ApplicationCall,
        parent: Association,
        limit: Long?,
        offset: Long?,
    ): List<SubscriptionInAssociation> {
        return getSubscriptionsInAssociationsSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

    override suspend fun listAdmin(call: ApplicationCall, parent: Association): Map<String, Any> {
        return mapOf(
            "items" to getSubscriptionsInAssociationsUseCase(parent.id),
            "stripe" to hasStripeAccountLinkedUseCase(parent)
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): SubscriptionInAssociation {
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

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
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
        id: UUID,
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

    override suspend fun checkout(call: ApplicationCall, parent: Association, id: UUID): CheckoutSession {
        val user = requireUserForCallUseCase(call) as User
        val subscription = getSubscriptionUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "subscriptions_in_associations_not_found"
        )
        return createCheckoutSessionUseCase(
            parent,
            user,
            listOf(
                CheckoutItem(
                    SubscriptionInAssociation::class.simpleName!!,
                    subscription.id,
                    subscription.name,
                    subscription.description,
                    1,
                    (subscription.price * 100).toLong()
                )
            ),
            "https://${call.request.host()}"
        ) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
